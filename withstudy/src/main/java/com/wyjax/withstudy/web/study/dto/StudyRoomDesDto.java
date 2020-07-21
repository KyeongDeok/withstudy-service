package com.wyjax.withstudy.web.study.dto;

import com.wyjax.withstudy.web.study.model.StudyRoom;
import com.wyjax.withstudy.web.study.model.StudyStatus;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class StudyRoomDesDto {
    private Long id;
    private String roomName;
    private String roomDesc;
    private String leader;
    private List<String> members;
    private List<String> techlist;
    private int limitNum;
    private int joinNum;
    private String dateTime;
    private StudyStatus status;
    private List<String> requests;

    public StudyRoomDesDto(StudyRoom studyRoom,
                           List<String> nickNames,
                           List<String> requests) {
        this.id = studyRoom.getId();
        this.roomName = studyRoom.getRoomName();
        this.roomDesc = studyRoom.getRoomDesc();
        this.leader = studyRoom.getMember().getNickname();
        this.members = nickNames;
        this.limitNum = studyRoom.getRoomLimit();
        this.joinNum = studyRoom.getJoinNum();
        this.dateTime = studyRoom.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = studyRoom.getStatus();
        this.techlist = new ArrayList<>();
        if (studyRoom.getTechList().length() > 0) {
            this.techlist = Arrays.asList(studyRoom.getTechList().split("/"));
        }
        this.requests = requests;
    }
}