package edu.nju.usm.controller;

import edu.nju.usm.command.MapMetaCommand;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.ResultMap;
import edu.nju.usm.model.User;
import edu.nju.usm.model.UserMapRelation;
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

import java.util.List;

/**
 * 地图接口
 *
 * @author YUF
 * @date 2018/01/14
 */
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
     * @return 返回用户故事地图列表
     */
    @GetMapping(value = "/list")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap getMapList() {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        } else {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .success()
                    .data("map_list", mapService.getMapList(user.getId()))
                    .message("获取成功!");
        }
    }

    /**
     * 创建用户故事地图
     *
     * @param mapMetaCommand 创建用户地图需要的参数封装
     * @return 返回创建的用户故事地图
     */
    @PostMapping(value = "/create")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap create(@RequestBody final MapMetaCommand mapMetaCommand) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        } else {
            Map map = mapService.createMap(mapMetaCommand.getName(), mapMetaCommand.getDescription(), user);
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .data("map", map)
                    .message("创建成功!");
        }
    }

    /**
     * 修改用户故事地图基本信息
     *
     * @param mapMetaCommand 用户故事地图元数据信息封装
     * @param map_id         用户故事地图id
     * @return 返回修改后的用户故事地图
     */
    @PostMapping(value = "/modify")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap modify(@RequestBody final MapMetaCommand mapMetaCommand, @RequestParam("map_id") long map_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        } else {
            Map map = mapService.getMapByUserIdMapId(user.getId(), map_id);
            if (map == null) {
                return new ResultMap().code(HttpStatus.OK.value())
                        .fail()
                        .message("地图不存在!");
            } else {
                if (map.getOwner_id() == user.getId()) {
                    map = mapService.modifyMap(map, mapMetaCommand.getName(), mapMetaCommand.getDescription());
                    return new ResultMap().code(HttpStatus.OK.value())
                            .success()
                            .data("map", map)
                            .message("修改成功!");
                } else {
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
     */
    @GetMapping(value = "/delete")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap delete(@RequestParam final long map_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        Map map = mapService.getMapByUserIdMapId(user.getId(), map_id);
        if (map == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("地图不存在!");
        } else {
            mapService.deleteMap(map, user);
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .message("删除成功!");
        }
    }

    // ========= edit by sunx ==========

    /**
     * 邀请协作者
     *
     * @param mapId 协作地图id
     * @param coUserId 协作者id
     * @return 邀请发送结果
     */
    @GetMapping(value = "/invite")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap inviteCollaborator(@RequestParam("map_id") long mapId,
                                        @RequestParam("co_user_id") long coUserId){
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        boolean success = mapService.inviteCollaborator(username, coUserId, mapId);
        if(success){
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .message("邀请已发送！");
        }else{
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .message("已经发送过邀请！");
        }
    }

    /**
     * 获取当前用户接收的协作邀请
     *
     * @param newOnly 是否只获取未处理的
     * @return 协作邀请列表
     */
    @GetMapping(value = "/received_invitations")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap getReceivedInvitations(@RequestParam("new_only") int newOnly){
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        boolean onlyNew = newOnly == 1;
        if(user != null){
            List<UserMapRelation> allInv = mapService.getReceivedInvitations(user.getId(), onlyNew);
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .success()
                    .data("invitations", allInv)
                    .message("获取成功！");
        }else{
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("认证错误！");
        }
    }


    /**
     * 获取当前用户为某地图发送的所有邀请
     *
     * @param mapId 地图
     * @return 邀请列表
     */
    @GetMapping(value = "/sent_invitations")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap getSentInvitations(@RequestParam("map_id") long mapId){
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if(user != null){
            List<UserMapRelation> sentInvs = mapService.getSentInvitations(mapId, user.getId());
            if(sentInvs != null){
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .success()
                        .data("invitations", sentInvs)
                        .message("获取成功！");
            }else {
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .fail()
                        .message("非当前地图拥有者！");
            }

        }else{
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("认证错误！");
        }
    }

    /**
     * 获取某地图的所有协作者
     * (默认不包含拥有者)
     *
     * @param mapId 地图
     * @return 协作者列表
     */
    @GetMapping(value = "/collaborators")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap getCollaborators(@RequestParam("map_id") long mapId){
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        List<User> cols = mapService.getCollaborators(mapId, username, false);
        if(cols != null){
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .success()
                    .data("collaborators", cols)
                    .message("获取成功！");
        }else {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("非协作者或拥有者，无权查看！");
        }
    }

    /**
     * 移除某个地图的某个协作者
     * 只有owner可以成功执行此操作
     *
     * @param mapId     地图
     * @param coUserId  协作者
     * @return 结果
     */
    @GetMapping(value = "/remove_collaborator")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap removeCollaborator(@RequestParam("map_id") long mapId,
                                        @RequestParam("user_id") long coUserId){
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        boolean success = mapService.deleteInvitation(username, coUserId, mapId);

        if(success){
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .success()
                    .message("成功！");
        }else{
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("没有权限！");
        }
    }

    /**
     * 响应邀请
     *
     * @param invId     user_map_relation id
     * @param response  1(接受) 2（拒绝）3（忽略）
     * @return 结果
     */
    @GetMapping(value = "/response_invitation")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap responseInvitation(@RequestParam("inv_id") long invId,
                                        @RequestParam("response") int response){
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        boolean success = mapService.responseInvitation(username, invId, response);

        if(success){
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .success()
                    .message("成功！");
        }else {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("邀请已关闭！");
        }
    }

}
