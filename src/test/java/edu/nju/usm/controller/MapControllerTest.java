package edu.nju.usm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nju.usm.command.MapMetaCommand;
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
        .andExpect(status().isOk());
//        .andExpect(jsonPath("data.map_list[0].map_name").value("test"))
//        .andExpect(jsonPath("data.map_list[1].map_name").value("test2"));
    }

    @Test
    @Transactional
    public void testCreate() throws Exception {
        String token = jwtUtils.createToken("user_search_test");
        MapMetaCommand command = new MapMetaCommand();
        command.setName("测试Map1");
        command.setDescription("测试描述1");
        this.mockMvc.perform(
                post("/map/create")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(command))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.map.map_name").value("测试Map1"))
        .andExpect(jsonPath("data.map.description").value("测试描述1"));
    }

    @Test
    @Transactional
    public void testModify() throws Exception {
        String token = jwtUtils.createToken("user_search_test");
        MapMetaCommand command = new MapMetaCommand();
        command.setName("测试Map2");
        command.setDescription("测试描述2");
        this.mockMvc.perform(
                post("/map/modify?map_id=1")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(command))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.map.map_name").value("测试Map2"))
        .andExpect(jsonPath("data.map.description").value("测试描述2"));

        this.mockMvc.perform(
                post("/map/modify")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(command))
        )
        .andExpect(status().isInternalServerError());
    }

    @Test
    public void testInviteCollaborator() throws Exception {
        String token = jwtUtils.createToken("user_search_test");
        this.mockMvc.perform(
                get("/map/invite")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("map_id", "1")
                        .param("co_user_id", "255")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }


    @Test
    public void testGetReceivedInvitations() throws Exception {
        String token = jwtUtils.createToken("sunx95");
        this.mockMvc.perform(
                get("/map/received_invitations")
                        .param("new_only", "1")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.invitations[0].response").value("0"));


        this.mockMvc.perform(
                get("/map/received_invitations")
                        .param("new_only", "0")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.invitations[0].response").value("0"));
    }


    @Test
    public void testGetSentInvitations() throws Exception {
        String token = jwtUtils.createToken("user_search_test");
        this.mockMvc.perform(
                get("/map/sent_invitations")
                        .param("map_id", "1")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.invitations[0].response").value("0"));
    }


    @Test
    public void testGetCollaborators() throws Exception {
        String token = jwtUtils.createToken("user_search_test");
        this.mockMvc.perform(
                get("/map/collaborators")
                        .param("map_id", "1")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveCollaborators() throws Exception {
        String token = jwtUtils.createToken("user_search_test");
        this.mockMvc.perform(
                get("/map/remove_collaborator")
                        .param("map_id", "1")
                        .param("user_id", "255")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                get("/map/invite")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("map_id", "1")
                        .param("co_user_id", "255")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void testResponseInvitation()throws Exception {
        String token = jwtUtils.createToken("sunx95");
        this.mockMvc.perform(
                get("/map/response_invitation")
                        .param("inv_id", "132")
                        .param("response", "1")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(
                get("/map/remove_collaborator")
                        .param("map_id", "1")
                        .param("user_id", "255")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(
                get("/map/invite")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("map_id", "1")
                        .param("co_user_id", "255")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }


}
