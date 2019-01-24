package edu.nju.usm.service;

import edu.nju.usm.mapper.MapMapper;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.User;
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
}
