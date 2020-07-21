package com.wyjax.withstudy.web.study.service;

import com.wyjax.withstudy.web.member.model.Member;
import com.wyjax.withstudy.web.member.model.MemberRepository;
import com.wyjax.withstudy.web.push.dto.PushRequestDto;
import com.wyjax.withstudy.web.push.service.PushService;
import com.wyjax.withstudy.web.study.model.repository.TechStackRepository;
import com.wyjax.withstudy.web.study.dto.StudyForm;
import com.wyjax.withstudy.web.study.dto.StudyRequestDto;
import com.wyjax.withstudy.web.study.dto.StudyRoomDesDto;
import com.wyjax.withstudy.web.study.dto.StudyRoomDto;
import com.wyjax.withstudy.web.study.model.StudyMember;
import com.wyjax.withstudy.web.study.model.StudyRequest;
import com.wyjax.withstudy.web.study.model.StudyRoom;
import com.wyjax.withstudy.web.study.model.StudyStatus;
import com.wyjax.withstudy.web.study.model.repository.StudyMemberRepository;
import com.wyjax.withstudy.web.study.model.repository.StudyRepository;
import com.wyjax.withstudy.web.study.model.repository.StudyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final TechStackRepository techStackRepository;
    private final StudyRequestRepository studyRequestRepository;
    private final PushService pushService;

    public StudyForm studyCreate(StudyForm studyForm, List<String> techList, String uid) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. : " + uid));
        StudyRoom studyRoom = null;

        if (isCreate(studyForm)) {
            String techString = techListFormat(techList);

            studyRoom = StudyRoom.builder()
                    .roomName(studyForm.getStudyName())
                    .roomDesc(studyForm.getStudyDesc())
                    .roomLimit(studyForm.getLimitNum())
                    .joinNumm(0)
                    .status(StudyStatus.READY)
                    .member(member)
                    .techs(techString)
                    .build();

            studyRepository.save(studyRoom);
            studyJoin(studyRoom.getId(), uid);
        }

        return (new StudyForm().toStudyForm(studyRoom));
    }

    /**
     * List<String> to String convert
     *
     * @param techList : 기술 스택 리스트
     * @return List to String
     */
    private String techListFormat(List<String> techList) {
        StringBuilder sb = new StringBuilder();

        if (techList != null) {
            for (int i = 0; i < techList.size(); i++) {
                sb.append(techList.get(i));
                if (i + 1 < techList.size())
                    sb.append("/");
            }
        }

        return (sb.toString());
    }

    /**
     * 스터디 개설 가능 확인
     */
    private boolean isCreate(StudyForm studyForm) {
        if (studyForm.getStudyName().trim().length() == 0)
            return false;
        else if (studyForm.getStudyDesc().trim().length() == 0)
            return false;
        else if (studyForm.getLimitNum() == 0)
            return false;

        return true;
    }

    @Transactional(readOnly = true)
    public List<StudyRoomDto> getStudyRoomList(Pageable pageable, Long lastId) {
        List<StudyRoom> studyRooms = ((lastId == null) ? studyRepository.findAllByOrderByIdDesc(pageable)
                : studyRepository.findByIdLessThanOrderByIdDesc(lastId, pageable));
        List<StudyRoomDto> studyRoomDtos = studyRooms.stream()
                .map(new Function<StudyRoom, StudyRoomDto>() {
                    @Override
                    public StudyRoomDto apply(StudyRoom studyRoom) {
                        StudyRoomDto studyRoomDto = new StudyRoomDto(studyRoom);
                        return studyRoomDto;
                    }
                }).collect(Collectors.toList());

        return studyRoomDtos;
    }

    /**
     * 1개의 스터디 룸을 가져온다.
     */
    @Transactional(readOnly = true)
    public StudyRoomDesDto getStudyOne(Long id) {
        StudyRoom studyRoom = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다. : " + id));

        return toStudyRoomDesDto(studyRoom);
    }

    /**
     * 스터디 신청
     */
    public void requestStudy(Long id, String uid) {
        StudyRoom studyRoom = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없습니다. : " + id));
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. : " + uid));

        pushAlert(studyRoom, member, id, uid);
    }

    /**
     * push 알람 보내기
     *
     * @param studyRoom : 스터디 신청한 studyRoom
     * @param member    : 신청한 사용자
     * @param id
     * @param uid
     */
    public void pushAlert(StudyRoom studyRoom, Member member, Long id, String uid) {
        StudyRequest studyRequest = studyRequestRepository.findByMemberIdAndStudyRoomId(member.getId(), studyRoom.getId());

        if (studyRequest == null) {
            studyRequest = StudyRequest.builder()
                    .member(member)
                    .studyRoom(studyRoom)
                    .build();
            studyRequestRepository.save(studyRequest);
            // Send Alert
            sendPush("님이 스터디 선청을 했습니다.",
                     "study/" + id,
                     studyRoom.getMember().getUid(),
                     uid);
        }
    }

    /**
     * 스터디 참여
     */
    public void studyJoin(Long id, String uid) {
        StudyRoom studyRoom = studyRepository.getOne(id);

        if (isStudyJoin(uid, studyRoom, studyRoom.getStudyMembers())) {
            studyRoom.memberJoin(addStudyMember(studyRoom, uid));
        }
    }

    /**
     * 스터디 참여가능 체크
     */
    public boolean isStudyJoin(String uid,
                               StudyRoom studyRoom,
                               List<StudyMember> studyMembers) {
        if (studyRoom == null) {
            return false;
        }
        if (studyRoom.getJoinNum() == studyRoom.getRoomLimit()) {
            return false;
        }
        for (StudyMember s : studyMembers) {
            if (uid.equals(s.getMemberId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 스터디 요청취소
     */
    public void studyCancel(Long id, String uid) {
        StudyRoom studyRoom = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다. : " + id));

        if (studyRoom.getJoinNum() > 0) {
            StudyRequest studyRequest = studyRequestRepository.findByMemberUidAndStudyRoomId(uid, id)
                    .orElseThrow(() -> new IllegalArgumentException("스터디요청이 없습니다."));
            studyRequestRepository.delete(studyRequest);
        }
    }

    /**
     * 스터디 삭제
     */
    public void studyDelete(Long id, String uid) {
        StudyRoom studyRoom = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다. : " + id));

        if (studyRoom == null) {
            return;
        }
        if (!studyRoom.getMember().getUid().equals(uid)) {
            return;
        }
        for (StudyMember sm : studyRoom.getStudyMembers()) {
            studyMemberRepository.delete(sm);
        }
        studyRepository.delete(studyRoom);
    }

    /**
     * 스터디 시작
     */
    public void studyStart(Long id, String uid) {
        StudyRoom studyRoom = studyRepository.getOne(id);

        if (studyRoom == null) {
            return;
        }
        if (!studyRoom.getMember().getUid().equals(uid)) {
            return;
        }
        if (studyRoom.getStatus() == StudyStatus.START) {
            return;
        }

        studyRoom.updateStatus(StudyStatus.START);
        studyRepository.save(studyRoom);
    }

    /**
     * 스터디 인원 추가
     */
    private StudyMember addStudyMember(StudyRoom studyRoom, String uid) {
        StudyMember studyMember = StudyMember.builder()
                .memberId(uid)
                .studyRoom(studyRoom)
                .build();

        return studyMember;
    }

    /**
     * Entity to DTO 변환
     */
    private StudyRoomDesDto toStudyRoomDesDto(StudyRoom studyRoom) {
        List<String> nickNames = new ArrayList<>();

        if (studyRoom.getStudyMembers().size() > 0)
            studyRoom.getStudyMembers().iterator().next();
        for (StudyMember m : studyRoom.getStudyMembers()) {
            String nickname = memberRepository.findByUid(m.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."))
                    .getNickname();

            if (nickname != null) {
                nickNames.add(nickname);
            }
        }
        List<String> requesters = new ArrayList<>();
        for (StudyRequest studyRequest : studyRoom.getStudyRequests()) {
            requesters.add(studyRequest.getMember().getUid());
        }

        return (new StudyRoomDesDto(studyRoom, nickNames, requesters));
    }

    /**
     * 자신이 만들었거나 참여중인 스터디 그룹을 가져온다.
     */
    @Transactional(readOnly = true)
    public List<StudyRoomDto> getMyStudyList(Pageable page, Long lastId, String uid) {
        List<StudyMember> studyMembers = (lastId == null ? studyMemberRepository.findAllByMemberIdOrderByIdDesc(uid, page) :
                studyMemberRepository.findByIdLessThanAndMemberIdOrderById(lastId, uid, page));
        List<StudyRoomDto> roomDtos = studyMembers.stream()
                .map(new Function<StudyMember, StudyRoomDto>() {
                    @Override
                    public StudyRoomDto apply(StudyMember studyMember) {
                        StudyRoomDto roomDto = new StudyRoomDto(studyMember.getStudyRoom());
                        return roomDto;
                    }
                }).collect(Collectors.toList());

        return roomDtos;
    }

    /**
     * 기술 검색
     */
    @Transactional(readOnly = true)
    public List<String> studySearch(String tech) {
        List<String> result = techStackRepository.searchTech(tech);
        Collections.sort(result);

        return result;
    }

    /**
     * 스터디 신청자 정보 가져오기
     */
    @Transactional(readOnly = true)
    public List<StudyRequestDto> getRequestList(Long id) {
        List<StudyRequestDto> studyRequestDtos = studyRequestRepository.findByStudyRoomId(id)
                .stream()
                .map(new Function<StudyRequest, StudyRequestDto>() {
                    @Override
                    public StudyRequestDto apply(StudyRequest studyRequest) {
                        StudyRequestDto studyRequestDto = new StudyRequestDto(studyRequest.getMember());
                        return studyRequestDto;
                    }
                }).collect(Collectors.toList());

        return studyRequestDtos;
    }

    public void sendPush(String message, String url, String toId, String fromId) {
        PushRequestDto pushRequestDto = PushRequestDto.builder()
                .message(message)
                .url(url)
                .toId(toId)
                .fromId(fromId)
                .build();
        pushService.messagePush(pushRequestDto);
    }
}