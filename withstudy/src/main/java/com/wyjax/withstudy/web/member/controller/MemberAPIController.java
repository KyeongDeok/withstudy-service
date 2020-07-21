package com.wyjax.withstudy.web.member.controller;

import com.wyjax.withstudy.config.auth.dto.SessionUser;
import com.wyjax.withstudy.web.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

import static com.wyjax.withstudy.web.member.controller.MemberController.SESSION_USER_ID;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class MemberAPIController {
    private final MemberService memberService;
    private final HttpSession session;

    /**
     * 닉네임 수정
     */
    @PostMapping("/change")
    public int nickChange(@RequestParam("toname") String toName) {
        SessionUser user = getSessionUser();
        int result = memberService.change_Name(toName, user.getUid());

        if (result == 2) {
            user.changeName(toName);
            session.setAttribute(SESSION_USER_ID, user);
        }

        return result;
    }

    /**
     * 포트폴리오 저장
     */
    @PostMapping("/portfolio")
    public Long savePortfolio(@RequestBody Map<String, String> data) {
        SessionUser user = getSessionUser();

        if (user == null) {
            return 0L;
        }

        return memberService.savePortFolio(data, user.getUid());
    }

    /**
     * 포트폴리오 삭제
     */
    @DeleteMapping("/portfolio")
    public void deletePortfolio(@RequestBody Map<String, Long> data) {
        SessionUser user = getSessionUser();
        memberService.deletePortFolio(data.get("id"), user.getUid());
    }

    private SessionUser getSessionUser() {
        return (SessionUser) session.getAttribute(SESSION_USER_ID);
    }
}
