package edu.nju.usm.command;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求封装
 *
 * @author HermC yzy627@126.com
 * @date 2018/01/05
 * @time 09:44
 * */
@Data
@NoArgsConstructor
public class LoginCommand {

    private String username;
    private String password;

}
