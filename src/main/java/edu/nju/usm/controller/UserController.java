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
import org.springframework.web.bind.annotation.*;

/**
 * 用户基础信息的查询、更新、修改路由
 *
 * @author HermC yzy627@126.com
 * @date 2018/01/08
 * @time 22:30
 * */
@RestController
@RequestMapping(value = "/user")
@CrossOrigin
public class UserController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    /**
     * 获取用户基础信息，不包括密码
     *
     * @return 返回用户基础信息
     * */
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

    /**
     * 用户注册方法
     *
     * @param userCommand 用户注册需要的信息封装
     * @return 如果用户已经存在，success返回false；如果注册失败，success返回false；如果注册成功，success返回true
     * */
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
