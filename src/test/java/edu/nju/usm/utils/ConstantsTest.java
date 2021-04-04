package edu.nju.usm.utils;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConstantsTest {

    @Test
    public void test() {
        assert Constants.SALT_LENGTH == 20;
        assert Constants.MD5.equals("MD5");
        assert Constants.NOT_BAN == 0;
        assert Constants.BAN == 1;
        assert Constants.ROLE_ADMIN.equals("admin");
        assert Constants.ROLE_USER.equals("user");
    }

}
