package edu.nju.usm.controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
public class AuthControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    public void authTest() throws Exception {
        this.mockMvc.perform(
                post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"user_search_test\", \"password\": \"123456\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("success").value(true))
        .andDo(document("auth",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("username").description("用户名(必须)").type(JsonFieldType.STRING),
                        fieldWithPath("password").description("密码(必须)").type(JsonFieldType.STRING)
                ),
                responseFields(
                        fieldWithPath("code").description("http状态码").type(JsonFieldType.NUMBER),
                        fieldWithPath("message").description("提示消息").type(JsonFieldType.STRING),
                        fieldWithPath("success").description("登录成功设为true；登录失败设为false，错误信息见message").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("data.token").description("认证token").type(JsonFieldType.STRING)
                ))
        );
    }

}
