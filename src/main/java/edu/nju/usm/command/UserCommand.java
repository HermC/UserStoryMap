package edu.nju.usm.command;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册请求封装
 *
 * @author HermC yzy627@126.com
 * @date 2018/01/08
 * @time 22:30
 * */
@Data
@NoArgsConstructor
public class UserCommand {

    private String username;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
