package org.sjtugo.api.UserTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetScheduleNormal() throws Exception{
        this.mockMvc.perform(post("/user/getScheduleInfo")
                    .param("userID","123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(Files.readString(Paths.get("./testcase/user/getScheduleInfoExpect.json"))));
    }

    @Test
    public void testGetScheduleNonExistUser() throws Exception{
        this.mockMvc.perform(post("/user/getScheduleInfo")
                .param("userID","234"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
