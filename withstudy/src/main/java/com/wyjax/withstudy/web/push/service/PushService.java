package com.wyjax.withstudy.web.push.service;

import com.wyjax.withstudy.web.push.dto.PushRequestDto;
import com.wyjax.withstudy.web.push.dto.PushResponseDto;
import com.wyjax.withstudy.web.push.model.Push;
import com.wyjax.withstudy.web.push.model.UserChannel;
import com.wyjax.withstudy.web.push.model.repository.PushRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PushService {
    private final PushRepository pushRepository;
    private final ConcurrentHashMap<String, UserChannel> map = new ConcurrentHashMap<>();

    public SseEmitter connectUser(String uid) throws InterruptedException {
        final SseEmitter emitter = new SseEmitter();
        UserChannel channel = new UserChannel(uid, emitter);

        try {
            map.put(uid, channel);
        }
        catch (Exception e) {
            throw new InterruptedException(e.getMessage());
        }
        emitter.onCompletion(() -> {
            map.remove(uid);
        });
        emitter.onTimeout(() -> {
            emitter.complete();
            map.remove(uid);
        });

        return emitter;
    }

    /**
     * 읽지 않은 알림 개수 가져오기
     */
    @Transactional(readOnly = true)
    public int getPushCount(String uid) {
        return pushRepository.getCountpush(uid).orElse(0);
    }

    /**
     * 알림 보내기
     */
    public void messagePush(PushRequestDto requestDto) {
        Push push = Push.builder()
                .message(requestDto.getMessage())
                .url(requestDto.getUrl())
                .toid(requestDto.getToId())
                .fromid(requestDto.getFromId())
                .isRead(false)
                .build();
        pushRepository.save(push);

        int count = pushRepository.getCountpush(push.getToid()).orElse(0);
        messagePush(push.getToid(), Integer.toString(count));
    }

    /**
     * 알림내용 가져오기
     */
    @Transactional(readOnly = true)
    public List<PushResponseDto> getPushList(String uid, Pageable page, Long lastId) {
        List<PushResponseDto> responseDtos = pushRepository.findByToidAndIdLessThanOrderByIdDesc(uid, lastId, page)
                .stream()
                .map(push -> new PushResponseDto(push))
                .collect(Collectors.toList());
        return responseDtos;
    }

    /**
     * 알림 확인
     */
    public String getPushContent(HttpSession session, Long id, String uid) {
        Push push = pushRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));
        pushUpdate(push);
        session.setAttribute("pushCount", getPushCount(uid));

        return push.getUrl();
    }

    public void pushUpdate(Push push) {
        push.update();
        pushRepository.save(push);
    }

    public void messagePush(String toId, String pushCount) {
        UserChannel channel = map.get(toId);

        if (channel == null) {
            throw new NullPointerException("사용자가 없습니다.");
        }
        channel.send(pushCount);
    }
}
