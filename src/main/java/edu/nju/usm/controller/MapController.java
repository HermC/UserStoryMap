package edu.nju.usm.controller;

import edu.nju.usm.command.MapMetaCommand;
import edu.nju.usm.command.UserCommand;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.ResultMap;
import edu.nju.usm.model.User;
import edu.nju.usm.service.MapService;
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
 * 地图接口
 *
 * @author YUF
 * @date 2018/01/14
 * */
@RestController
@RequestMapping(value = "/map")
@CrossOrigin
public class MapController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private MapService mapService;

    /**
     * 获取用户故事地图列表
     *
     * @param username 用户名
     * @return 返回用户故事地图列表
     * */
    @GetMapping(value = "/query")
    @RequiresRoles(logical = Logical.OR, value = { Constants.ROLE_ADMIN, Constants.ROLE_USER })
    public ResultMap getMapList(@RequestParam("username") String username) {
        return new ResultMap()
                .code(HttpStatus.OK.value())
                .success()
                .data("map_list", mapService.getMapList(username))
                .message("获取成功!");
    }

    /**
     * 创建用户故事地图
     *
     * @param mapMetaCommand 用户注册需要的信息封装
     * @return 如果用户已经存在，success返回false；如果注册失败，success返回false；如果注册成功，success返回true
     * */
    @PostMapping(value = "/create")
    @RequiresRoles(logical = Logical.OR, value = { Constants.ROLE_ADMIN, Constants.ROLE_USER })
    public ResultMap create(@RequestBody final MapMetaCommand mapMetaCommand) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user==null){
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        }
        else{
            Map map=mapService.createMap(mapMetaCommand.getName(),mapMetaCommand.getDescription(),user);
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .data("map",map)
                    .message("创建成功!");
        }
    }

    /**
     * 修改用户故事地图基本信息
     *
     * @param mapMetaCommand 用户故事地图元数据信息封装
     * @param map_id 用户故事地图id
     * @return 如果用户已经存在，success返回false；如果注册失败，success返回false；如果注册成功，success返回true
     * */
    @PostMapping(value = "/modify")
    @RequiresRoles(logical = Logical.OR, value = { Constants.ROLE_ADMIN, Constants.ROLE_USER })
    public ResultMap modify(@RequestBody final MapMetaCommand mapMetaCommand,@RequestParam("map_id") long map_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user==null){
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        }
        else{
            Map map=mapService.getMapByUserIdMapId(user.getId(),map_id);
            if(map==null){
                return new ResultMap().code(HttpStatus.OK.value())
                        .fail()
                        .message("地图不存在!");
            }
            else{
                if(map.getOwner_id()==user.getId()) {
                    map=mapService.modifyMap(map, mapMetaCommand.getName(), mapMetaCommand.getDescription());
                    return new ResultMap().code(HttpStatus.OK.value())
                            .success()
                            .data("map", map)
                            .message("修改成功!");
                }
                else{
                    return new ResultMap().code(HttpStatus.OK.value())
                            .fail()
                            .message("没有权限!");
                }
            }
        }
    }

    /**
     * 删除用户故事地图
     *
     * @param map_id 用户故事地图id
     * @return
     * */
    @GetMapping(value = "/delete")
    @RequiresRoles(logical = Logical.OR, value = { Constants.ROLE_ADMIN, Constants.ROLE_USER })
    public ResultMap delete(@RequestParam final long map_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        Map map=mapService.getMapByUserIdMapId(user.getId(),map_id);
        if (map==null){
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("地图不存在!");
        }
        else{
            mapService.deleteMap(map,user);
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .message("删除成功!");
        }
    }
}
