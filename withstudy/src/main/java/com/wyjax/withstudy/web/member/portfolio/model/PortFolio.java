package com.wyjax.withstudy.web.member.portfolio.model;

import com.wyjax.withstudy.web.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class PortFolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public PortFolio(String name,
                     String url,
                     Member member) {
        this.name = name;
        this.url = url;
        this.member = member;
    }
}
