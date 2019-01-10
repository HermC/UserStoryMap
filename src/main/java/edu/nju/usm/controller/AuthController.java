package edu.nju.usm.controller;

import edu.nju.usm.command.LoginCommand;
import edu.nju.usm.command.ModifyPasswordCommand;
import edu.nju.usm.model.ResultMap;
import edu.nju.usm.service.AuthService;
import edu.nju.usm.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 权限获取、验证、更新路由
 *
 * @author HermC yzy627@126.com
 * @date 2018/01/05
 * @time 09:44
 * */
@RestController
@RequestMapping(value = "/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthService authService;

    /**
     * 根据用户名密码获取用户认证token
     *
     * @param command LoginCommand
     * @return 如果用户和密码验证失败，返回错误；如果用户密码正确，返回认证token
     * */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResultMap auth(@RequestBody final LoginCommand command) {
        if (!authService.verifyUser(command.getUsername())) {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        }
        if (!authService.verifyPassword(command.getUsername(), command.getPassword())) {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("密码错误!");
        }
        return new ResultMap()
                .code(HttpStatus.OK.value())
                .success()
                .data("token", jwtUtils.createToken(command.getUsername()))
                .message("认证成功!");
    }

    /**
     * 修改用户密码
     *
     * */
    @RequestMapping(value = "/password/modify", method = RequestMethod.POST)
    public ResultMap modifyPassword(@RequestBody final ModifyPasswordCommand command) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        if (!authService.verifyUser(username)) {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        }
        if (!authService.verifyPassword(username, command.getOldPassword())) {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("密码错误!");
        }
        int rows = authService.modifyPassword(username, command.getOldPassword(), command.getNewPassword());
        if (rows == 0) {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("修改失败!");
        } else {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .success()
                    .message("修改成功!");
        }
    }

}
