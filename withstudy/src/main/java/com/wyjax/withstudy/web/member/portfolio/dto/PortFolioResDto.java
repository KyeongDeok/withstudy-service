package com.wyjax.withstudy.web.member.portfolio.dto;

import com.wyjax.withstudy.web.member.portfolio.model.PortFolio;
import lombok.Getter;

@Getter
public class PortFolioResDto {
    private Long id;
    private String name;
    private String url;

    public PortFolioResDto(PortFolio portFolio) {
        this.id = portFolio.getId();
        this.name = portFolio.getName();
        this.url = portFolio.getUrl();
    }
}
