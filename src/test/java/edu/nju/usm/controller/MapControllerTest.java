package edu.nju.usm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nju.usm.command.UserCommand;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
public class MapControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .addFilters((Filter) context.getBean("shiroFilter"))
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    public void getMapList() throws Exception {
        String token = jwtUtils.createToken("user_search_test");
        // 测试正常获取
        this.mockMvc.perform(
                get("/map/list")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.map_list[0].map_name").value("test"))
                .andExpect(jsonPath("data.map_list[1].map_name").value("test2"));
    }
    @Test
    public void testToken() throws Exception {
        System.out.println(jwtUtils.createToken("user_search_test"));//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NDc2MjczODIsInVzZXJuYW1lIjoidXNlcl9zZWFyY2hfdGVzdCJ9.Nkd88ztgioMvvUhN1L30oUmE8be0Vkxmv7ynQVBbMCM
        //System.out.println(jwtUtils.createToken("user_search_test3"));//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NDc2MjI0NDAsInVzZXJuYW1lIjoidXNlcl9zZWFyY2hfdGVzdDMifQ.Y1bHVXWqR3Q53sqSZu5ZuCL0jXhX7cIEEjBIrsGhhMY
        //System.out.println(jwtUtils.createToken("user_search_test2"));//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NDc2MjM3MjEsInVzZXJuYW1lIjoidXNlcl9zZWFyY2hfdGVzdDIifQ.-JGZfOFaJ9EXmi3xs_mc4nzFTbPCbGBCZXVUBSL9Ivw
    }
}
