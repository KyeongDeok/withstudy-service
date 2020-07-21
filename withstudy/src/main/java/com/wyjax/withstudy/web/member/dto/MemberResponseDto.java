package com.wyjax.withstudy.web.member.dto;

import com.wyjax.withstudy.web.common.domain.TimeConvert;
import com.wyjax.withstudy.web.member.model.Member;
import com.wyjax.withstudy.web.member.portfolio.dto.PortFolioResDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemberResponseDto extends TimeConvert {
    private String uid;
    private String name;
    private String picture;
    private String serverName;
    private String createDate;
    private List<PortFolioResDto> portFolioResDtos;

    public MemberResponseDto(Member m) {
        this.uid = m.getUid();
        this.name = m.getNickname();
        this.createDate = timeConvert(m.getCreateDate());
        this.portFolioResDtos = new ArrayList<>();
        this.picture = m.getPicture();
        this.serverName = m.getServerName();

        for (int i = 0; i < m.getPortFolios().size(); i++) {
            PortFolioResDto portFolioResDto = new PortFolioResDto(m.getPortFolios().get(i));
            portFolioResDtos.add(portFolioResDto);
        }
    }
}