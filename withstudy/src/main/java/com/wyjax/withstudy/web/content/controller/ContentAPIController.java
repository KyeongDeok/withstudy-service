package com.wyjax.withstudy.web.content.controller;

import com.wyjax.withstudy.config.auth.dto.SessionUser;
import com.wyjax.withstudy.web.content.dto.ContentRequestDto;
import com.wyjax.withstudy.web.content.dto.ContentResponseDto;
import com.wyjax.withstudy.web.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class ContentAPIController {
    private final ContentService contentService;
    private final HttpSession session;

    /**
     * 게시판 저장
     */
    @PostMapping("/board")
    public Long addContent(@RequestBody ContentRequestDto requestDto) {
        SessionUser user = (SessionUser) session.getAttribute("user");

        if (user.getUid() == null) {
            return Long.valueOf(0);
        }
        return contentService.save(requestDto, user.getUid());
    }

    /**
     * 게시판 수정
     */
    @PutMapping("/board/{id}")
    public Long updateContent(@PathVariable Long id, @RequestBody ContentRequestDto requestDto) {
        return contentService.update(id, requestDto);
    }

    /**
     * 게시판 삭제
     */
    @DeleteMapping("/board/{id}")
    public void delContent(@PathVariable Long id) {
        SessionUser user = (SessionUser) session.getAttribute("user");

        if (contentService.userCheck(id, user.getUid())) {
            contentService.delete(id);
        }
    }
}
