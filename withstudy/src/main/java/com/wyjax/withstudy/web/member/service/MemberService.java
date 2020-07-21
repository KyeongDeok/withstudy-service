package com.wyjax.withstudy.web.member.service;

import com.wyjax.withstudy.web.member.dto.MemberRequestDto;
import com.wyjax.withstudy.web.member.dto.MemberResponseDto;
import com.wyjax.withstudy.web.member.model.Member;
import com.wyjax.withstudy.web.member.model.MemberRepository;
import com.wyjax.withstudy.web.member.portfolio.model.PortFolio;
import com.wyjax.withstudy.web.member.portfolio.model.PortFolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PortFolioRepository portFolioRepository;


    public String join(MemberRequestDto requestDto) {
        Member member = Member.builder()
                .uid(requestDto.getUid())
                .nickname(requestDto.getNickname())
                .build();

        if (validateDuplicateMember(member)) {
            memberRepository.save(member);
        }
        return memberRepository.findByUid(requestDto.getUid())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."))
                .getNickname();
    }

    /**
     * 중복된 회원 확인
     */
    private boolean validateDuplicateMember(Member member) {
        Member members = memberRepository.findByUid(member.getUid()).get();

        return (members == null);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMember(String uid) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. uid=" + uid));
        MemberResponseDto memberDto = new MemberResponseDto(member);

        return memberDto;
    }

    public int change_Name(String toName, String uid) {
        Member isExsit = memberRepository.findByNickname(toName);

        if (isExsit != null) {
            return 1;
        }

        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. uid=" + uid));
        member.nickCahnge(toName);
        memberRepository.save(member);

        return 2;
    }

    public Long savePortFolio(Map<String, String> data, String uid) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다."));

        PortFolio portFolio = PortFolio.builder()
                .name(data.get("name"))
                .url(data.get("url"))
                .member(member)
                .build();

        return portFolioRepository.save(portFolio).getId();
    }

    public String deletePortFolio(Long id, String uid) {
        PortFolio portFolio = portFolioRepository.findByIdAndMember_Uid(id, uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다."));

        if (portFolio == null) {
            return "N";
        }
        portFolioRepository.delete(portFolio);
        return "Y";
    }
}