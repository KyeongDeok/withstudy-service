package com.wyjax.withstudy.config.auth.service;

import com.wyjax.withstudy.config.auth.dto.OAuthAttribute;
import com.wyjax.withstudy.config.auth.dto.SessionUser;
import com.wyjax.withstudy.web.member.model.Member;
import com.wyjax.withstudy.web.member.model.MemberRepository;
import com.wyjax.withstudy.web.push.model.repository.PushRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final PushRepository pushRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService userService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = userService.loadUser(userRequest);

        String registerId = userRequest.getClientRegistration().getRegistrationId();
        String userName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttribute attributes = OAuthAttribute.of(registerId, userName, oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);

        int pushCount = pushRepository.getCountpush(member.getUid())
                .orElse(0);

        httpSession.setAttribute("user", new SessionUser(member));
        httpSession.setAttribute("pushCount", pushCount);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole())),
                                     attributes.getAttributes(),
                                     attributes.getNameAttributeKey());
    }

    private Member saveOrUpdate(OAuthAttribute attributes) {
        Member user = memberRepository.findByUid(attributes.getUid())
                .map(m -> m.update(attributes.getPicture()))
                .orElse(attributes.toEntity());

        return memberRepository.save(user);
    }
}

