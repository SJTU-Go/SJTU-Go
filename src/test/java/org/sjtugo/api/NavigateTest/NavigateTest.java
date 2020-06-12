package org.sjtugo.api.NavigateTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NavigateTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void busShouldReturnNormal() throws Exception {
        this.mockMvc.perform(post("/navigate/bus").contentType(MediaType.APPLICATION_JSON).content(
                Files.readString(Paths.get("./testcase/navigate/NormalTestCase.json"))
        )).andDo(print())
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/result/MockMvcResultHandlers.html
        .andExpect(status().isOk())
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/result/MockMvcResultMatchers.html
        .andExpect(content().json(Files.readString(Paths.get("./testcase/navigate/NormalTestCaseExpect.json"))));
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/result/ContentResultMatchers.html#json-java.lang.String-
    }
}
