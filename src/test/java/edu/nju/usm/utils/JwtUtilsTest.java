package edu.nju.usm.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void createTokenTest() {
        System.out.println(jwtUtils.createToken("Username_test"));
    }

    @Test
    public void verifyTest() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NDY2NTE1MzQsInVzZXJuYW1lIjoiVXNlcm5hbWVfdGVzdCJ9.i1qkUeaiucBEgeEwwydDO0U-32fm1KGrlaR-goEYu_A";
        String username = "Username_test";

        // 该token已超时
        assert !jwtUtils.verify(token, username);

        token = jwtUtils.createToken("user_token_test");
        assert jwtUtils.verify(token, "user_token_test");
    }

    @Test
    public void verifyTimeout() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NDY2NTE1MzQsInVzZXJuYW1lIjoiVXNlcm5hbWVfdGVzdCJ9.i1qkUeaiucBEgeEwwydDO0U-32fm1KGrlaR-goEYu_A";

        System.out.println("Timeout: " + jwtUtils.verifyTimeout(token));
    }

    @Test
    public void getUsernameTest() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NDY2NTE1MzQsInVzZXJuYW1lIjoiVXNlcm5hbWVfdGVzdCJ9.i1qkUeaiucBEgeEwwydDO0U-32fm1KGrlaR-goEYu_A";
        String username = "Username_test";

        assert jwtUtils.getUsername(token).equals(username);
    }

}
