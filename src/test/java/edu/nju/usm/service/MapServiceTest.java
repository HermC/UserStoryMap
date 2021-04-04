package edu.nju.usm.service;

import edu.nju.usm.mapper.MapMapper;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.User;
import edu.nju.usm.model.UserMapRelation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapServiceTest {

    @Autowired
    private MapService mapService;
    @Autowired
    private MapMapper mapMapper;
    @Autowired
    private UserService userService;

    @Test
    public void getMapListTest() {
        List<Map> mapList_result = mapService.getMapList(2);
        for (int i = 0; i < mapList_result.size(); i++) {
            System.out.println(mapList_result.get(i).getId());
        }
    }

    @Test
    public void getMapByUserIdMapIdTest() {
        Map result = mapService.getMapByUserIdMapId(2, 2);
        assert result.getMap_name().equals("test2");
    }

    @Test
    @Transactional
    public void createMapTest() {
        User user = userService.getUser("user_search_test");
        Map result = mapService.createMap("autotest", "autotest", user);
        assert result.getMap_name().equals("autotest");
    }

    @Test
    @Transactional
    public void modifyMapTest() {
        Map map = mapService.getMapByUserIdMapId(2, 2);
        mapService.modifyMap(map, "modifytest", "modifytest");
        Map result = mapService.getMapByUserIdMapId(2, 2);
        assert result.getMap_name().equals("modifytest");
    }

    @Test
    @Transactional
    public void deleteMapTest() {
        User user74 = userService.getUser("user_register_test");
        Map map2 = mapService.getMapByUserIdMapId(74, 2);
        mapService.deleteMap(map2, user74);
        assert mapMapper.findByMapId(2) != null;
        assert mapMapper.findUserMapRelationByUseridAndMapId(2, 74) == null;

        User user = userService.getUser("user_search_test");
        Map map = mapService.getMapByUserIdMapId(2, 2);
        mapService.deleteMap(map, user);
        assert mapService.getMapByUserIdMapId(2, 2) == null;
    }


    @Test
    public void inviteCollaboratorTest() {
        boolean success = true;

        //normal
//        success = mapService.inviteCollaborator("user_search_test", 255l, 1l);
//        assert success;

        //not owner
        success = mapService.inviteCollaborator("user_search_test", 255l, 84);
        assert !success;

        // already
//        success = mapService.inviteCollaborator("user_search_test", 255l, 1l);
//        assert !success;
    }

    @Test
    public void getReceivedInvitationTest(){
        mapService.inviteCollaborator("user_search_test", 255l, 1l);
        List<UserMapRelation> invs = mapService.getReceivedInvitations(255l, true);
        assert invs.size() == 1;
    }

    @Test
    public void getSentInvitationTest(){
        List<UserMapRelation> invs = null;
        // owner(2) owns map(1)
        invs = mapService.getSentInvitations(1l, 2l);
        assert invs.size() > 0;
        // owner(1) does not own map(2)
        invs = mapService.getSentInvitations(2l, 1l);
        assert invs == null;
    }

    @Test
    public void getCollaboratorsTest(){
        List<User> cols = null;
        cols = mapService.getCollaborators(1l, "user_search_test", false);
        assert cols.size() == 0;
        cols = mapService.getCollaborators(1l, "user_search_test",true);
        assert cols.size() == 1;
        cols = mapService.getCollaborators(1l, "sunx95", true);
        assert cols == null;
    }

    @Test
    public void responseInvitation(){
        boolean success = false;

//        success = mapService.responseInvitation("sunx95", 112l, 1);
//        assert success;

        success = mapService.deleteInvitation("user_search_test", 255l, 1l);
        assert success;

        success = mapService.inviteCollaborator("user_search_test", 255l, 1);
        assert success;
    }

    @Test
    public void removeCollaboratorTest(){
        boolean success = false;

        success = mapService.deleteInvitation("sunx95", 255l, 1l);
        assert !success;

        success = mapService.deleteInvitation("user_search_test", 255l, 1l);
        assert success;

        success = mapService.inviteCollaborator("user_search_test", 255l, 1);
        assert success;
    }


}
