package com.wyjax.withstudy.web.content.service;

import com.wyjax.withstudy.web.content.dto.ContentRequestDto;
import com.wyjax.withstudy.web.content.dto.ContentResponseDto;
import com.wyjax.withstudy.web.content.model.Content;
import com.wyjax.withstudy.web.content.model.repository.ContentRepository;
import com.wyjax.withstudy.web.member.model.Member;
import com.wyjax.withstudy.web.member.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<ContentResponseDto> getBoardList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");
        Page<ContentResponseDto> responseDtos = contentRepository.findByIsdelete(pageable, 'N')
                .map(new Function<Content, ContentResponseDto>() {
                    @Override
                    public ContentResponseDto apply(Content content) {
                        ContentResponseDto responseDto = new ContentResponseDto(content);
                        return responseDto;
                    }
                });

        return responseDtos;
    }

    public Long save(ContentRequestDto requestDto, String uid) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. uid=" + uid));

        Content content = Content.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .member(member)
                .build();
        content = contentRepository.save(content);

        return content.getId();
    }

    public Long update(Long id, ContentRequestDto requestDto) {
        Content content = contentRepository
                .findById(id).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + id));
        content.update(requestDto.getTitle(), requestDto.getContent());

        return content.getId();
    }

    @Transactional(readOnly = true)
    public ContentResponseDto view(Long id) {
        Content content = contentRepository.findByIdAndAndIsdelete(id, 'N');
        ContentResponseDto responseDto = new ContentResponseDto(content);

        return responseDto;
    }

    public void delete(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("존재하지 않음"));
        content.delete();
    }

    public void countUp(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("존재하지 않음"));
        content.viewCountUp();
        contentRepository.save(content);
    }

    public Boolean userCheck(Long id, String uid) {
        String dif_uid = view(id).getMember().getUid();

        return (uid.equals(dif_uid));
    }
}