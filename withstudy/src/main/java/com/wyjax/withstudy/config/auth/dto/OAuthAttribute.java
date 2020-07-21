package com.wyjax.withstudy.config.auth.dto;

import com.wyjax.withstudy.web.member.model.Member;
import com.wyjax.withstudy.web.member.model.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttribute {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String uid;
    private String name;
    private String serverName;
    private String picture;

    @Builder
    public OAuthAttribute(Map<String, Object> attributes, String uid, String serverName,
                          String nameAttrKey, String name, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttrKey;
        this.uid = uid;
        this.name = name;
        this.serverName = serverName;
        this.picture = picture;
    }

    public static OAuthAttribute of(String registrationId, String userNameAttributeName,
                                    Map<String, Object> attributes) {
        if ("github".equals(registrationId)) {
            return ofGithub(registrationId, userNameAttributeName, attributes);
        }
        return ofGoogle(registrationId, userNameAttributeName, attributes);
    }

    public static OAuthAttribute ofGoogle(String registrationId,
                                          String userNameAttributeName,
                                          Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                .uid(attributes.getOrDefault("sub", "").toString())
                .name(attributes.getOrDefault("name", "").toString() + "@" + registrationId)
                .serverName(registrationId)
                .picture(attributes.getOrDefault("picture", "").toString())
                .attributes(attributes)
                .nameAttrKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttribute ofGithub(String registrationId,
                                          String userNameAttributeName,
                                          Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                .uid(attributes.getOrDefault("id", "").toString())
                .name(attributes.getOrDefault("login", "").toString() + "@" + registrationId)
                .serverName(registrationId)
                .picture(attributes.getOrDefault("avatar_url", "").toString())
                .attributes(attributes)
                .nameAttrKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .uid(uid)
                .nickname(name)
                .serverName(serverName)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}
