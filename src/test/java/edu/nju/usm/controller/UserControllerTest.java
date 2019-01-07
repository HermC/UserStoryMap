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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
public class UserControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .addFilters((Filter) context.getBean("shiroFilter"))
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    public void getUserInfoTest() throws Exception {
        String token = jwtUtils.createToken("user_search_test");
        // 测试正常获取
        this.mockMvc.perform(
                get("/user")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.user.username").value("user_search_test"))
        .andDo(document("getUserInfo",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("code").description("http状态码").type(JsonFieldType.NUMBER),
                        fieldWithPath("message").description("提示消息").type(JsonFieldType.STRING),
                        fieldWithPath("success").description("获取成功设为true；获取失败设为false，错误信息见message").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("data.user.id").description("用户ID").type(JsonFieldType.NUMBER),
                        fieldWithPath("data.user.username").description("用户名").type(JsonFieldType.STRING),
                        fieldWithPath("data.user.email").description("用户邮箱").type(JsonFieldType.STRING),
                        fieldWithPath("data.user.ban").description("账号状态，0代表正常，1代表被封号").type(JsonFieldType.NUMBER),
                        fieldWithPath("data.user.roles").description("用户角色").type(JsonFieldType.ARRAY)
        )));

        // 测试没有权限
        this.mockMvc.perform(
                get("/user")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("code").value(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @Transactional
    public void registerTest() throws Exception {
        // 测试正常注册
        this.mockMvc.perform(
                post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"user_insert_test\", \"password\": \"123456\", \"email\": \"user_insert_test@test.com\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.user.username").value("user_insert_test"))
        .andDo(document("register",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("username").description("用户名(必须)").type(JsonFieldType.STRING),
                        fieldWithPath("password").description("密码(必须)").type(JsonFieldType.STRING),
                        fieldWithPath("email").description("邮箱(必须)").type(JsonFieldType.STRING)
                ),
                responseFields(
                        fieldWithPath("code").description("http状态码").type(JsonFieldType.NUMBER),
                        fieldWithPath("message").description("提示消息").type(JsonFieldType.STRING),
                        fieldWithPath("success").description("注册成功设为true；注册失败设为false，错误信息见message").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("data.user.id").description("用户ID").type(JsonFieldType.NUMBER),
                        fieldWithPath("data.user.username").description("用户名").type(JsonFieldType.STRING),
                        fieldWithPath("data.user.email").description("用户邮箱").type(JsonFieldType.STRING),
                        fieldWithPath("data.user.ban").description("账号状态，0代表正常，1代表被封号").type(JsonFieldType.NUMBER),
                        fieldWithPath("data.user.roles").description("用户角色").type(JsonFieldType.ARRAY)
        )));

        // 测试用户存在
        this.mockMvc.perform(
                post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"user_search_test\", \"password\": \"123456\", \"email\": \"user_insert_test@test.com\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("message").value("该用户名已存在!"));

        // 测试没有用户名
        this.mockMvc.perform(
                post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\": \"123456\", \"email\": \"user_insert_test@test.com\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("message").value("注册失败!"));
    }

}
