package com.wyjax.withstudy.web.study.controller;

import com.wyjax.withstudy.config.auth.dto.SessionUser;
import com.wyjax.withstudy.web.study.dto.StudyForm;
import com.wyjax.withstudy.web.study.dto.StudyRoomDesDto;
import com.wyjax.withstudy.web.study.dto.StudyRoomDto;
import com.wyjax.withstudy.web.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.wyjax.withstudy.web.member.controller.MemberController.SESSION_USER_ID;

@Controller
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final HttpSession session;

    @GetMapping("")
    public String studyList(Model model) {
        List<StudyRoomDto> studyRoomDtos = studyService.getStudyRoomList(PageRequest.of(0, 15), null);
        setModelAttribute(studyRoomDtos, model, "allapi");

        return "studys/studyList";
    }

    @GetMapping("/mine")
    public String myStudy(Model model) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);
        List<StudyRoomDto> studyRoomDtos = studyService.getMyStudyList(PageRequest.of(0, 15), null, user.getUid());
        setModelAttribute(studyRoomDtos, model, "mineapi");

        return "studys/studyList";
    }

    /**
     * Model 추가
     * @param studyRoomDtos
     * @param model
     * @param prePath : 유입 uri
     */
    public void setModelAttribute(List<StudyRoomDto> studyRoomDtos, Model model, String prePath) {
        model.addAttribute("studyRoomDtos", studyRoomDtos);
        model.addAttribute("prepath", prePath);

        if (studyRoomDtos.size() > 0) {
            model.addAttribute("lastId", studyRoomDtos.get(studyRoomDtos.size() - 1).getId());
        }
    }

    @GetMapping("/new")
    public String createStudy(Model model) {
        model.addAttribute("studyForm", new StudyForm());

        return "studys/createStudy";
    }

    @PostMapping("/new")
    public String studyRegister(@RequestParam(value = "techs", required = false) List<String> techList,
                                StudyForm studyForm, Model model) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);
        StudyForm retForm = studyService.studyCreate(studyForm, techList, user.getUid());

        if (retForm == null)
            return "redirect:/";

        model.addAttribute("studyForm", retForm);

        return "redirect:/study/" + retForm.getId();
    }

    @GetMapping("/{id}")
    public String studyView(@PathVariable(value = "id") Long studyNum, Model model) {
        StudyRoomDesDto studyRoomDesDto = studyService.getStudyOne(studyNum);
        model.addAttribute("roomInfo", studyRoomDesDto);

        return "studys/studyView";
    }
}