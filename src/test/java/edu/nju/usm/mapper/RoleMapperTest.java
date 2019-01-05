package edu.nju.usm.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void findTest() {
        assert roleMapper.find(2L).size() > 0;
    }

    @Test
    @Transactional
    public void insertTest() {
        assert roleMapper.insert(2L, "user") == 1;
        assert roleMapper.find(2L).size() > 1;
    }

    @Test
    @Transactional
    public void deleteTest() {
        assert roleMapper.delete(2L, "admin") == 1;
        assert roleMapper.delete(2L, "user") == 0;
    }

}
