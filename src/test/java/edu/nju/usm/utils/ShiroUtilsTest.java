package edu.nju.usm.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroUtilsTest {

    @Test
    public void generateSaltTest() {
        System.out.println(ShiroUtils.generateSalt(20));
    }

    @Test
    public void encryptPasswordTest() {
        String defaultPassword = "123456";
        String password1 = ShiroUtils.encryptPassword("MD5", defaultPassword, "salt");
        String password2 = ShiroUtils.encryptPassword("MD5", defaultPassword, "salt");
        assert password1.equals(password2);

        System.out.println(ShiroUtils.encryptPassword("MD5", "", "salt"));
    }

}
