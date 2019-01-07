package edu.nju.usm.controller;

import edu.nju.usm.command.UserCommand;
import edu.nju.usm.model.ResultMap;
import edu.nju.usm.model.User;
import edu.nju.usm.service.UserService;
import edu.nju.usm.utils.Constants;
import edu.nju.usm.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @RequiresRoles(logical = Logical.OR, value = { Constants.ROLE_ADMIN, Constants.ROLE_USER })
    public ResultMap getUserInfo() {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        return new ResultMap()
                .code(HttpStatus.OK.value())
                .success()
                .data("user", userService.getUser(username))
                .message("获取成功!");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultMap register(@RequestBody final UserCommand userCommand) {
        User user = new User();
        user.setUsername(userCommand.getUsername());
        user.setPassword(userCommand.getPassword());
        user.setEmail(userCommand.getEmail());
        if (userService.isExisted(user.getUsername())) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("该用户名已存在!");
        }
        int rows = userService.addUser(user);
        if (rows == 0) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("注册失败!");
        } else {
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .data("user", user)
                    .message("注册成功!");
        }
    }

}
