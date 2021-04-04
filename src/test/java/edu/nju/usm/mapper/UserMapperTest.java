package edu.nju.usm.mapper;

import edu.nju.usm.model.User;
import edu.nju.usm.utils.Constants;
import edu.nju.usm.utils.ShiroUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void findTest() {
        assert userMapper.find("user_test_not_exist") == null;

        User user = userMapper.find("user_search_test");
        assert user.getEmail().equals("user_search_test@test.com");
        System.out.println(user.getRoles().size());
        assert user.getRoles().size() > 0;
    }

    /**
     * 使用 @Transactional 来保证数据库回滚
     * */
    @Test
    @Transactional
    public void insertTest() {
        User user = new User();
        user.setUsername("user_insert_test");
        user.setEmail("user_insert_test@test.com");
        user.setSalt(ShiroUtils.generateSalt(Constants.SALT_LENGTH));
        user.setPassword(ShiroUtils.encryptPassword(Constants.MD5, "123456", user.getSalt()));
        user.setBan(Constants.NOT_BAN);
        assert userMapper.insert(user) == 1;
    }

    @Test
    @Transactional
    public void updateTest() {
        User user = userMapper.find("user_search_test");
        user.setBan(Constants.BAN);
        assert userMapper.update(user) == 1;
        User user2 = userMapper.find("user_search_test");
        assert user.getBan() == user2.getBan();
    }

    @Test
    @Transactional
    public void deleteTest() {
        assert userMapper.delete("user_search_test") == 1;
        assert userMapper.find("user_search_test") == null;
    }

}
