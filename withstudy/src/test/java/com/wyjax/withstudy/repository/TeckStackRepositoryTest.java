package com.wyjax.withstudy.repository;

import com.wyjax.withstudy.web.study.model.TechStack;
import com.wyjax.withstudy.web.study.model.repository.TechStackRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeckStackRepositoryTest {
    @Autowired
    TechStackRepository techStackRepository;

    @After
    public void clean_up() {
        techStackRepository.deleteAll();
    }

    @Test
    public void 기술스택_저장() {
        // given
        String techName = "Java";
        techStackRepository.save(
                TechStack.builder()
                        .name(techName)
                        .build()
        );
        // when
        TechStack techStack = techStackRepository.findAll().get(0);
        // then
        assertThat(techStack.getName()).isEqualTo(techName);
    }

    @Test
    public void Like_테스트() {
        // given
        int total = 5;
        for (int i = 0; i < total; i++) {
            techStackRepository.save(
                    TechStack.builder()
                            .name("test" + i)
                            .build()
            );
        }
        // when
        int cnt = techStackRepository.searchTech("test").size();
        // then
        assertThat(cnt).isEqualTo(total);
    }
}
