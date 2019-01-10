package edu.nju.usm.mapper;

import edu.nju.usm.model.Map;
import edu.nju.usm.model.User;
import edu.nju.usm.utils.Constants;
import edu.nju.usm.utils.ShiroUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapMapperTest {
    private final Logger log = LoggerFactory.getLogger(MapMapperTest.class);
    @Autowired
    private MapMapper mapMapper;

    @Test
    public void findByUserIdTest() {
        List<Long> result1=mapMapper.findByUserId(3);
        if(result1!=null) {
            log.info(result1.toString());
        }
        assert result1.size() == 0;

        List<Long> real2 = new ArrayList<>();
        real2.add(1l);
        real2.add(2l);
        List<Long> result2 = mapMapper.findByUserId(2);
        if(result2!=null) {
            log.info(result2.toString());
        }
        assert real2.equals(result2);
    }

    @Test
    public void findByMapIdsTest() {
        Map result1=mapMapper.findByMapId(1l);
        if(result1!=null) {
            log.info(result1.toString());
        }
        Map real1 = new Map();
        real1.setMap_name("test");
        assert real1.getMap_name().equals(result1.getMap_name());
    }
}
