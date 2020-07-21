package com.wyjax.withstudy.web.content.controller;

import com.wyjax.withstudy.config.auth.dto.SessionUser;
import com.wyjax.withstudy.web.content.dto.ContentRequestDto;
import com.wyjax.withstudy.web.content.dto.ContentResponseDto;
import com.wyjax.withstudy.web.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class ContentController {
    private final String BOARD_READ_PATH = "board";
    private final String CONTENT_ATTRIBUTE_NAME = "content";

    private final ContentService contentService;
    private final HttpSession session;

    @GetMapping("")
    public String contentList(@PageableDefault Pageable pageable, Model model) {
        Page<ContentResponseDto> contents = contentService.getBoardList(pageable);
        model.addAttribute("contentList", contents);

        return "boards/boardList";
    }

    @GetMapping("/new")
    public String getWritePage(Model model) {
        model.addAttribute(CONTENT_ATTRIBUTE_NAME, new ContentRequestDto());

        return "boards/writePage";
    }

    /**
     * 게시판 보기
     */
    @GetMapping("/{id}")
    public String viewContent(@PathVariable("id") Long id,
                              Model model,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        ContentResponseDto responseDto = contentService.view(id);

        if (responseDto != null) {
            response.addCookie(viewCountUp(request.getCookies(), id));
            model.addAttribute(CONTENT_ATTRIBUTE_NAME, responseDto);
            return "boards/viewPage";
        }

        return "redirect:/board";
    }

    /**
     * 게시판 뷰카운트
     */
    private Cookie viewCountUp(Cookie[] cookies, Long id) {
        boolean check = true;
        String thisCookie = BOARD_READ_PATH + id;
        Cookie cookie = null;

        for (Cookie c : cookies) {
            if (!c.getName().equals(thisCookie)) {
                continue;
            }
            check = false;
            cookie = c;
            break;
        }
        if (check) {
            contentService.countUp(id);
            cookie = new Cookie(thisCookie, thisCookie);
        }

        return cookie;
    }

    /**
     * 게시판 편집
     */
    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        SessionUser user = (SessionUser) session.getAttribute("user");

        if (user == null) {
            return "redirect:/board";
        }
        if (contentService.userCheck(id, user.getUid())) {
            ContentResponseDto content = contentService.view(id);
            model.addAttribute("id", id);
            model.addAttribute(CONTENT_ATTRIBUTE_NAME, content);

            return "boards/editPage";
        }

        return "redirect:/board";
    }
}