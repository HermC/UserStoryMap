package edu.nju.usm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(locations = {"classpath*:application.yml"})
public class UsmApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println("Hook test");
        System.out.println("Test success!");
    }

}

