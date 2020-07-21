package com.wyjax.withstudy.web.study.controller;

import com.wyjax.withstudy.config.auth.dto.SessionUser;
import com.wyjax.withstudy.web.study.dto.StudyRequestDto;
import com.wyjax.withstudy.web.study.dto.StudyRoomDto;
import com.wyjax.withstudy.web.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.wyjax.withstudy.web.member.controller.MemberController.SESSION_USER_ID;

@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyAPIController {
    private final StudyService studyService;
    private final HttpSession session;


    /**
     * 스터디 신청
     */
    @PostMapping("/members")
    public String studyJoin(@RequestBody Long id) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);

        if (user.getUid() != null) {
            studyService.requestStudy(id, user.getUid());
        }

        return "join";
    }

    /**
     * 스터디 신청취소
     */
    @DeleteMapping("/members")
    public String studyCancel(@RequestBody Long id) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);

        if (user.getUid() != null) {
            studyService.studyCancel(id, user.getUid());
        }

        return "cancel";
    }

    /**
     * 스터디 시작
     */
    @PutMapping("/status")
    public String studyStart(@RequestBody Long id) {
        SessionUser user = (SessionUser) Optional.ofNullable(session.getAttribute(SESSION_USER_ID))
                .orElseThrow(() -> new NullPointerException());
        studyService.studyStart(id, user.getUid());

        return "start";
    }

    /**
     * 스터디 삭제
     */
    @DeleteMapping("/status")
    public String studyDelete(@RequestBody Long id) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);

        if (user.getUid() != null && id != null) {
            studyService.studyDelete(id, user.getUid());
        }

        return "delete";
    }

    /**
     * 무한 스크롤
     */
    @GetMapping("/allapi")
    public List<StudyRoomDto> allAPI(int size, Long lastId) {
        return studyService.getStudyRoomList(PageRequest.of(0, size), lastId);
    }

    /**
     * 무한 스크롤
     */
    @GetMapping("/mineapi")
    public List<StudyRoomDto> mineAPI(@PageableDefault Pageable pageable, Long lastId) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);
        user = Optional.ofNullable(user)
                .orElseThrow(() -> new NullPointerException("인증되지 않은 사용자입니다,"));

        return studyService.getMyStudyList(pageable, lastId, user.getUid());
    }

    /**
     * 기술스택 불러오기(검색)
     */
    @GetMapping("/techs/{tech}")
    public List<String> techSearch(@PathVariable("tech") String name) {
        List<String> result = studyService.studySearch(name);

        return result;
    }

    /**
     * 스터디 신청자 불러오기
     */
    @GetMapping("/{id}/request")
    public List<StudyRequestDto> studyRequestList(@PathVariable(name = "id") Long id) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);

        if (user == null) {
            return null;
        }

        return studyService.getRequestList(id);
    }

    @PutMapping("/{id}/request")
    public HttpStatus studyRequestAccept(@PathVariable("id") Long id,
                                         @RequestBody Map<String, String> data) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);

        if (user == null) {
            throw new IllegalStateException("사용자가 존재하지 않습니다.");
        }

        studyService.studyJoin(id, data.get("uid"));
        studyService.studyCancel(id, data.get("uid"));

        return HttpStatus.OK;
    }

    @DeleteMapping("/{id}/request")
    public HttpStatus studyRequestDelete(@PathVariable("id") Long id,
                                         @RequestBody Map<String, String> data) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);

        if (user == null) {
            throw new IllegalStateException("사용자가 존재하지 않습니다.");
        }

        studyService.studyCancel(id, data.get("uid"));

        return HttpStatus.OK;
    }
}
