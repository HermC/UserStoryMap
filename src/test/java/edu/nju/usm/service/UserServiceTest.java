package edu.nju.usm.service;

import edu.nju.usm.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUserTest() {
        assert userService.getUser("user_not_exist") == null;
        assert userService.getUser("user_search_test").getUsername().equals("user_search_test");
    }

    @Test
    public void searchUserTest(){
        assert userService.searchUser("") == null;
        assert userService.searchUser(null) == null;
        assert userService.searchUser("sunx").size() == 2;
        assert userService.searchUser("o~~~lala").size() == 0;
    }


    @Test
    @Transactional
    public void addUserTest() {
        assert userService.addUser(null) == 0;

        User user = new User();
        user.setId(-1L);
        assert userService.addUser(user) == 0;

        user.setUsername("user_search_test");
        assert userService.addUser(user) == 0;
        user.setPassword("123456");
        assert userService.addUser(user) == 0;
        user.setEmail("user_search_test@test.com");
        assert userService.addUser(user) == 0;  // 由于用户存在导致依然返回0

        user.setUsername("user_search_test_new");
        assert userService.addUser(user) == 1;
        assert user.getId() != -1L;
        assert user.getRoles().size() > 0;
    }

    @Test
    @Transactional
    public void modifyUserInfoTest() {
        assert userService.modifyUserInfo(null) == 0;

        User user = new User();
        assert userService.modifyUserInfo(user) == 0;
        user.setUsername("user_search_test");
        user.setEmail("user_modify_test@test.com");
        assert userService.modifyUserInfo(user) == 1;

        assert userService.getUser("user_search_test").getEmail().equals("user_modify_test@test.com");
    }

}
