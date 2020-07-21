package com.wyjax.withstudy.web.study.dto;

import com.wyjax.withstudy.web.study.model.StudyRoom;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudyForm {
    Long id;
    String studyName;
    String studyDesc;
    int limitNum;
    int joinNum;

    public StudyForm toStudyForm(StudyRoom studyRoom) {
        this.id = studyRoom.getId();
        this.studyName = studyRoom.getRoomName();
        this.studyDesc = studyRoom.getRoomDesc();
        this.limitNum = studyRoom.getRoomLimit();
        this.joinNum = limitNum - studyRoom.getStudyMembers().size();

        return this;
    }
}