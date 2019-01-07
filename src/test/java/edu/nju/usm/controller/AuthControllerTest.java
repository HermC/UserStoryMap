package edu.nju.usm.controller;

import edu.nju.usm.utils.JwtUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

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
    @Autowired
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .addFilters((Filter) context.getBean("shiroFilter"))
                .build();
    }

    @Test
    public void authTest() throws Exception {
        // 测试正常登陆
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

        // 测试用户不存在
        this.mockMvc.perform(
                post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"user_not_exist\", \"password\": \"12345\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("message").value("用户不存在!"));

        // 测试密码错误
        this.mockMvc.perform(
                post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"user_search_test\", \"password\": \"12345\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("message").value("密码错误!"));
    }

    @Test
    @Transactional
    public void modifyPasswordTest() throws Exception {
        // 测试正常密码修改
        String token = jwtUtils.createToken("user_search_test");
        this.mockMvc.perform(
                post("/auth/password/modify")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\": \"123456\", \"newPassword\": \"1234567\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("success").value(true))
        .andDo(document("modifyPassword",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("oldPassword").description("旧密码(必须)").type(JsonFieldType.STRING),
                        fieldWithPath("newPassword").description("新密码(必须)").type(JsonFieldType.STRING)
                ),
                responseFields(
                        fieldWithPath("code").description("http状态码").type(JsonFieldType.NUMBER),
                        fieldWithPath("message").description("提示消息").type(JsonFieldType.STRING),
                        fieldWithPath("success").description("修改成功设为true；修改失败设为false，错误信息见message").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("data").description("无返回数据").type(JsonFieldType.OBJECT)
                )
        ));
    }

    @Test
    @Transactional
    public void modifyPasswordTestAddon() throws Exception {
        // 测试用户不存在
        String token = jwtUtils.createToken("user_not_exist");
        this.mockMvc.perform(
                post("/auth/password/modify")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\": \"123456\", \"newPassword\": \"1234567\"}")
        )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("code").value(HttpStatus.UNAUTHORIZED.value()))
        .andExpect(jsonPath("message").value("用户不存在!"));

        // 测试密码错误
        token = jwtUtils.createToken("user_search_test");
        this.mockMvc.perform(
                post("/auth/password/modify")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\": \"12345\", \"newPassword\": \"1234567\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("message").value("密码错误!"));

        // 测试修改失败
        token = jwtUtils.createToken("user_search_test");
        this.mockMvc.perform(
                post("/auth/password/modify")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\": \"123456\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("message").value("修改失败!"));
    }

}
