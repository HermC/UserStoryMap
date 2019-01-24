package edu.nju.usm.mapper;

import edu.nju.usm.model.Map;
import edu.nju.usm.model.UserMapRelation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapMapperTest {
    private final Logger log = LoggerFactory.getLogger(MapMapperTest.class);
    @Autowired
    private MapMapper mapMapper;

    @Test
    @Transactional
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
    }

    @Test
    @Transactional
    public void findByMapIdTest() {
        Map result1=mapMapper.findByMapId(1l);
        if(result1!=null) {
            log.info(result1.toString());
        }
        Map real1 = new Map();
        real1.setMap_name("test");
        assert real1.getMap_name().equals(result1.getMap_name());
    }

    @Test
    @Transactional
    public void findUserMapRelationByMapIddTest() {
        List<UserMapRelation> result1=mapMapper.findUserMapRelationByMapId(1l);
        if(result1!=null) {
            log.info(result1.toString());
        }
        UserMapRelation real1 = new UserMapRelation();
        real1.setUser_id(2);
        assert result1.size()==1;
        assert real1.getUser_id()==result1.get(0).getUser_id();
    }

    @Test
    @Transactional
    public void insertTest() {
        Date nowDate=new Date();
        Map map1=new Map();
        map1.setOwner_id(3);
        map1.setCreated_time(new java.sql.Date(nowDate.getTime()));
        map1.setDescription("test insert");
        map1.setMap_name("test insert");
        int result=mapMapper.insert(map1);
        assert result==1;
    }

    @Test
    @Transactional
    public void insertUserMapRelationTest() {
        UserMapRelation userMapRelation=new UserMapRelation();
        userMapRelation.setUser_id(136);
        userMapRelation.setMap_id(2);
        int result=mapMapper.insertUserMapRelation(userMapRelation);
        assert result==1;
    }

    @Test
    @Transactional
    public void deleteTest() {
        int result=mapMapper.delete(2);
        assert result==1;
    }

    @Test
    @Transactional
    public void deleteUserMapRelationByUseridAndMapIdTest() {
        int result=mapMapper.deleteUserMapRelationByUseridAndMapId(2,2);
        assert result==1;
    }

    @Test
    @Transactional
    public void deleteUserMapRelationTest() {
        int result=mapMapper.deleteUserMapRelation(2);
//        assert result==1;
    }
}
