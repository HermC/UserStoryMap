package edu.nju.usm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void verifyUserTest() {
        assert authService.verifyUser("user_search_test");
        assert !authService.verifyUser("user_not_exist");
    }

    @Test
    public void verifyPasswordTest() {
        assert !authService.verifyPassword(null, "123456");
        assert !authService.verifyPassword("", "123456");
        assert !authService.verifyPassword("user_not_exist", "123456");
        assert authService.verifyPassword("user_search_test", "123456");
    }

    @Test
    @Transactional
    public void modifyPasswordTest() {
        assert authService.modifyPassword(null, "123456", "123456") == 0;
        assert authService.modifyPassword("", "123456", "123456") == 0;
        assert authService.modifyPassword("", "123456", "123456") == 0;
        assert authService.modifyPassword("user_not_exist", "123456", "123456") == 0;
        assert authService.modifyPassword("user_search_test", null, "123456") == 0;
        assert authService.modifyPassword("user_search_test", "123456", null) == 0;
        assert authService.modifyPassword("user_search_test", "", "12345") == 0;

        assert authService.modifyPassword("user_search_test", "123456", "123457") == 1;
        assert authService.verifyPassword("user_search_test", "123457");
    }

}
