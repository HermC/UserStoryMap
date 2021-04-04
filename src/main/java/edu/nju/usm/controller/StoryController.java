package edu.nju.usm.controller;

import edu.nju.usm.command.StoryCommand;
import edu.nju.usm.model.*;
import edu.nju.usm.service.MapService;
import edu.nju.usm.service.ReleaseService;
import edu.nju.usm.service.StoryService;
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
 * 故事接口
 *
 * @author YUF
 * @date 2018/01/15
 */
@RestController
@RequestMapping(value = "/story")
@CrossOrigin
public class StoryController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private MapService mapService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private ReleaseService releaseService;

    /**
     * 获取用户故事详情
     *
     * @param story_id 故事id
     * @return 返回用户故事详情
     */
    @GetMapping(value = "/query")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap getStoryDetail(@RequestParam("story_id") long story_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        } else {
            Story story = storyService.getStoryById(story_id);
            if (story == null) {
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .fail()
                        .message("id不存在");
            }
            Map map = mapService.getMapByUserIdMapId(user.getId(), story.getMap_id());
            if (map == null) {
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .fail()
                        .message("没有查看权限");
            } else {
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .success()
                        .data("story", story)
                        .message("获取成功!");
            }
        }
    }

    /**
     * 获取用户故事列表
     *
     * @param map_id 地图id
     * @return 返回用户故事列表
     */
    @GetMapping(value = "/list")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap getStoryList(@RequestParam("map_id") long map_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        } else {
            Map map = mapService.getMapByUserIdMapId(user.getId(), map_id);
            if (map == null) {
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .fail()
                        .message("没有查看权限");
            }
            List<Story> storyList = storyService.getStoryList(map_id);
            List<Release> releaseList  =releaseService.getReleaseList(map_id);
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .success()
                    .data("storyList", storyList)
                    .data("releaseList", releaseList)
                    .message("获取成功!");
        }
    }

    /**
     * 创建用户故事
     *
     * @param storyCommand 故事的信息封装
     * @return 返回新建的用户故事
     */
    @PostMapping(value = "/create")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap create(@RequestBody final StoryCommand storyCommand) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        } else {
            Map map = mapService.getMapByUserIdMapId(user.getId(), storyCommand.getMap_id());
            if (map == null) {
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .fail()
                        .message("没有创建权限");
            }
            Story story = storyService.createStory(storyCommand.getStory_name(), storyCommand.getDescription(), storyCommand.getMap_id(), storyCommand.getParent_story_id(), storyCommand.getStory_type(), storyCommand.getStory_status(), user);
            if (story == null) {
                return new ResultMap().code(HttpStatus.OK.value())
                        .fail()
                        .message("创建失败!");
            } else {
                return new ResultMap().code(HttpStatus.OK.value())
                        .success()
                        .data("story", story)
                        .message("创建成功!");
            }
        }
    }

    /**
     * 修改故事 名称 描述 类型 状态
     *
     * @param storyCommand 故事的信息封装
     * @return 返回修改后的用户故事
     * @Param story_id 故事id
     */
    @PostMapping(value = "/modify")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap modify(@RequestBody final StoryCommand storyCommand, @RequestParam("story_id") long story_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        }
        Story story = storyService.getStoryById(story_id);
        if (story == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("故事不存在!");
        }
        Map map = mapService.getMapByUserIdMapId(user.getId(), story.getMap_id());
        if (map == null) {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("没有修改权限");
        }
        story = storyService.modifyStory(story_id, storyCommand);
        if (story == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("修改失败!");
        } else {
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .data("story", story)
                    .message("修改成功!");
        }

    }

    /**
     * 修改用户故事 父故事
     *
     * @param story_id  故事id
     * @param parent_id 父故事id
     * @return 返回修改后的用户故事
     */
    @GetMapping(value = "/modifyparent")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap modifyParent(@RequestParam("story_id") long story_id, @RequestParam("parent_id") long parent_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        if (user == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("用户不存在!");
        } else {
            Story story = storyService.getStoryById(story_id);
            if (story == null) {
                return new ResultMap().code(HttpStatus.OK.value())
                        .fail()
                        .message("故事不存在!");
            }
            Map map = mapService.getMapByUserIdMapId(user.getId(), story.getMap_id());
            if (map == null) {
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .fail()
                        .message("没有修改权限");
            }
            Story newStory = storyService.modifyStoryParent(story_id, parent_id);
            if (newStory == null) {
                return new ResultMap()
                        .code(HttpStatus.OK.value())
                        .fail()
                        .message("修改失败");
            }
            return new ResultMap().code(HttpStatus.OK.value())
                    .success()
                    .data("story", newStory)
                    .message("修改成功!");
        }
    }

    /**
     * 修改用户故事发布
     *
     * @param story_id   故事id
     * @param release_id 发布id
     * @return 返回修改后的用户故事
     */
    @GetMapping(value = "/modifyrelease")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap modifyRelease(@RequestParam("story_id") long story_id, @RequestParam("release_id") long release_id) {
        //TODO
        return new ResultMap().code(HttpStatus.OK.value())
                .fail()
                .message("暂不支持!");
    }

    /**
     * 删除用户故事
     *
     * @param story_id 故事id
     */
    @GetMapping(value = "/delete")
    @RequiresRoles(logical = Logical.OR, value = {Constants.ROLE_ADMIN, Constants.ROLE_USER})
    public ResultMap delete(@RequestParam("story_id") final long story_id) {
        String username = jwtUtils.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.getUser(username);
        Story story = storyService.getStoryById(story_id);
        if (story == null) {
            return new ResultMap().code(HttpStatus.OK.value())
                    .fail()
                    .message("故事不存在!");
        }
        Map map = mapService.getMapByUserIdMapId(user.getId(), story.getMap_id());
        if (map == null) {
            return new ResultMap()
                    .code(HttpStatus.OK.value())
                    .fail()
                    .message("没有删除权限!");
        }
        storyService.deleteStory(story.getId());
        return new ResultMap().code(HttpStatus.OK.value())
                .success()
                .message("删除成功!");

    }
}
