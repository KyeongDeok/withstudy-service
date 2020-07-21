package com.wyjax.withstudy.web.member.controller;

import com.wyjax.withstudy.web.member.dto.MemberResponseDto;
import com.wyjax.withstudy.web.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    public static final String SESSION_USER_ID = "user";

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String getInfo(@PathVariable(value = "id") String id, Model model) {
        MemberResponseDto memberDto = memberService.getMember(id);

        if (memberDto != null) {
            model.addAttribute("userinfo", memberDto);
            return "members/userInfo";
        }

        return "redirect:/";
    }
}