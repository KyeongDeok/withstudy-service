package com.wyjax.withstudy.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyjax.withstudy.config.auth.dto.SessionUser;
import com.wyjax.withstudy.web.content.dto.ContentRequestDto;
import com.wyjax.withstudy.web.content.model.Content;
import com.wyjax.withstudy.web.content.model.repository.ContentRepository;
import com.wyjax.withstudy.web.member.model.Member;
import com.wyjax.withstudy.web.member.model.MemberRepository;
import com.wyjax.withstudy.web.member.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class XssSpringBootTest {

    @LocalServerPort
    int port;

    private MockHttpSession session;
    private MockHttpServletRequest request;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        session = new MockHttpSession();
        Member member = Member.builder()
                .uid("test")
                .role(Role.USER)
                .picture("test")
                .nickname("test")
                .serverName("test")
                .build();
        memberRepository.save(member);
        session.setAttribute("user", new SessionUser(member));
        request = new MockHttpServletRequest();
        request.setSession(session);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void session_테스트() {
        // given
        String name = "test";
        // when
        SessionUser user = (SessionUser) session.getAttribute("user");
        // then
        assertThat(user.getUid()).isEqualTo(name);
    }

    @Test
    @WithMockUser("USER")
    public void index_리턴() throws Exception {
        String hello = "hello";

        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void 태그_치환() throws Exception {
        // given
        String title = "<li>hi</li>";
        String content = "<li>hi</li>";
        String expected = "&lt;li&gt;hi&lt;/li&gt;";
        ContentRequestDto dto = new ContentRequestDto(title, content);

        String url = "http://localhost:" + port + "/board";

        // when
        mvc.perform(post(url)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .session(session)
                            .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());

        // then
        List<Content> all = contentRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expected);
        assertThat(all.get(0).getContent()).isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void RESPONSE_태그() {
        String content = "<li>hi</li>";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("content", content);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void 요청_반환() {
        String content = "<li>content</li>";
        String expected = "&lt;li&gt;content&lt;/li&gt;";
    }
}
