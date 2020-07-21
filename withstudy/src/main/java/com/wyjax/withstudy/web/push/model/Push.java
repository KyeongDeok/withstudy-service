package com.wyjax.withstudy.web.push.model;

import com.wyjax.withstudy.web.common.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Push extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String message;

    @Column
    private String url;

    @Column
    private String toid;

    @Column
    private String fromid;

    @Column
    private boolean isRead;

    @Builder
    public Push(String message,
                String url,
                String toid,
                String fromid,
                boolean isRead) {
        this.message = message;
        this.url = url;
        this.toid = toid;
        this.fromid = fromid;
        this.isRead = isRead;
    }

    public void update() {
        this.isRead = true;
    }
}
