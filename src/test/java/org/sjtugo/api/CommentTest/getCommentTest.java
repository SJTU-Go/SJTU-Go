package org.sjtugo.api.CommentTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class getCommentTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetCommentByLocation() throws Exception{
        this.mockMvc.perform(post("/comments/getcomments/loc")
                .param("location",Files.readString(Paths.get("./testcase/comment/getCommentByLocParam.json"))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(Files.readString(Paths.get("./testcase/comment/getCommentByLocExpect.json"))));
    }

    @Test
    public void testGetCommentByPlaceID() throws Exception

}
