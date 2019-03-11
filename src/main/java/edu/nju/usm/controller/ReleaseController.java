package edu.nju.usm.controller;

import edu.nju.usm.command.ReleaseCommand;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.Release;
import edu.nju.usm.model.ResultMap;
import edu.nju.usm.model.User;
import edu.nju.usm.service.MapService;
import edu.nju.usm.service.ReleaseService;
import edu.nju.usm.service.UserService;
import edu.nju.usm.utils.Constants;
import edu.nju.usm.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/release")
@CrossOrigin
public class ReleaseController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ReleaseService releaseService;
    @Autowired
    private UserService userService;
    @Autowired
    private MapService mapService;

    @PostMapping(value = "/create")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap create(@RequestBody final ReleaseCommand releaseCommand) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");

        } else {
            Map map = mapService.getMapByUserIdMapId(user.getId(), releaseCommand.getMap_id());
            if (map == null) {
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .fail()
                        .message("没有创建权限");
            }
            Release release = releaseService.createRelease(releaseCommand.getMap_id(), releaseCommand.getRelease_name(), releaseCommand.getDeadline());
            if (release == null) {
                return new ResultMap().code(HttpStatus.OK.value())
                        .fail()
                        .message("创建失败!");
            } else {
                return new ResultMap().code(HttpStatus.OK.value())
                        .success()
                        .data("release", release)
                        .message("创建成功!");
            }
        }
    }

    @PostMapping(value = "/modify")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap modify(@RequestBody final ReleaseCommand releaseCommand) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        }
        Release release = releaseService.getReleaseById(releaseCommand.getMap_id());
        if (release == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("Release不存在!");
        }
        Map map = mapService.getMapByUserIdMapId(user.getId(), release.getMap_id());
        if (map == null) {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("没有修改权限");
        }
        release = releaseService.modify(releaseCommand);
        if (release == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("修改失败!");
        } else {
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .data("release", release)
                    .message("修改成功!");
        }
    }

    @GetMapping(value = "/delete")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap delete(@RequestParam("release_id") final long release_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        }
        Release release = releaseService.getReleaseById(release_id);
        if (release == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("Release不存在!");
        }
        Map map = mapService.getMapByUserIdMapId(user.getId(), release.getMap_id());
        if (map == null) {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("没有删除权限!");
        }
        int result = releaseService.deleteRelease(release_id);
        if (result != 1) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("删除失败!请确保该Release下没有任何用户故事!");
        } else {
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .message("删除成功!");
        }
    }

}
