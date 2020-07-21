package com.wyjax.withstudy.repository;

import com.wyjax.withstudy.web.member.model.Member;
import com.wyjax.withstudy.web.member.model.MemberRepository;
import com.wyjax.withstudy.web.member.model.Role;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @After
    public void clean_up() {
        memberRepository.deleteAll();
    }

    @Test
    public void 유저_저장() {
        // given
        String uid = "test";
        String nick = "test";

        memberRepository.save(Member.builder()
                                      .uid("test")
                                      .nickname("test")
                                      .picture("test")
                                      .role(Role.USER)
                                      .build());
        // when
        List<Member> members = memberRepository.findAll();
        // then
        assertThat(members.get(0).getUid()).isEqualTo("test");
        assertThat(members.get(0).getNickname()).isEqualTo("test");
    }

    @Test
    public void 유저_닉네임변경() {
        // given
        Member member = memberRepository.save(Member.builder()
                                                      .uid("test")
                                                      .nickname("test")
                                                      .picture("test")
                                                      .role(Role.USER)
                                                      .build());
        String to_nickname = "닉네임변경";
        Member m = memberRepository.findAll().get(0);
        m.nickCahnge(to_nickname);

        // when
        List<Member> members = memberRepository.findAll();

        // then
        member.nickCahnge(to_nickname);
        assertThat(member.getNickname()).isEqualTo(to_nickname);
    }

    @Test
    public void 유저_그림변경() {
        // given
        Member member = memberRepository.save(Member.builder()
                                                      .uid("test")
                                                      .nickname("test")
                                                      .picture("test")
                                                      .role(Role.USER)
                                                      .build());
        String picture = "그림변경";
        member = member.update(picture);
        memberRepository.save(member);

        // when
        Member member1 = memberRepository.findAll().get(0);

        // then
        assertThat(member1.getPicture()).isEqualTo(picture);
    }
}
