package com.wyjax.withstudy.web.study.dto;

import com.wyjax.withstudy.web.common.domain.TimeConvert;
import com.wyjax.withstudy.web.study.model.StudyRoom;
import com.wyjax.withstudy.web.study.model.StudyStatus;
import lombok.Getter;

@Getter
public class StudyRoomDto extends TimeConvert {
    private Long id;
    private String studyName;
    private int limitMember;
    private int joinMember;
    private StudyStatus status;
    private String regTime;

    public StudyRoomDto(StudyRoom studyRoom) {
        this.id = studyRoom.getId();
        this.studyName = studyRoom.getRoomName();
        this.limitMember = studyRoom.getRoomLimit();
        this.joinMember = studyRoom.getJoinNum();
        this.status = studyRoom.getStatus();
        this.regTime = timeConvert(studyRoom.getCreateDate());
    }
}