package edu.nju.usm.service;

import edu.nju.usm.mapper.MapMapper;
import edu.nju.usm.mapper.UserMapper;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.User;
import edu.nju.usm.model.UserMapRelation;
import edu.nju.usm.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地图信息处理
 *
 * @author YUF
 * @date 2018/01/14
 */
@Service
public class MapService {

    @Autowired
    private MapMapper mapMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 通过用户id查找地图列表
     * sunx: 包括拥有的和协作的
     *
     * @param user_id 用户id
     * @return 地图列表
     */
    public List<Map> getMapList(long user_id) {
        User user = userMapper.findById(user_id);
        List<Long> mapIdList = mapMapper.findByUserId(user_id);
        List<Map> mapList = new ArrayList<>();
        for (int i = 0; i < mapIdList.size(); i++) {
            long mapId = mapIdList.get(i);
            mapList.add(mapMapper.findByMapId(mapId));
        }
        return mapList.stream().map(m -> {
            m.setCollaborators(this.getCollaborators(m.getId(), user.getUsername(), false));
            return m;
        }).collect(Collectors.toList());
    }

    /**
     * 通过用户id和地图id查找地图
     *
     * @param user_id 用户id
     * @param map_id  地图id
     * @return 地图
     */
    public Map getMapByUserIdMapId(long user_id, long map_id) {

        // 优先返回user创建的map
        Map map = mapMapper.findByUserIdMapId(user_id, map_id);

        if (map == null) {
            UserMapRelation userMapRelation = mapMapper.findUserMapRelationByUseridAndMapId(map_id, user_id);
            if (userMapRelation != null && userMapRelation.isAccepted()) {
                map = mapMapper.findByMapId(map_id);
            }
        }

        return map;
    }

    /**
     * 创建用户故事地图
     *
     * @param name        地图名称
     * @param description 地图描述
     * @param user        创建者
     * @return 地图列表
     */
    @Transactional
    public Map createMap(String name, String description, User user) {
        Map map = new Map();
        map.setMap_name(name);
        map.setDescription(description);
        map.setOwner_id(user.getId());
        map.setCreated_time(new java.sql.Date(new Date().getTime()));
        int result = mapMapper.insert(map);

        // comment by sunx
//        UserMapRelation userMapRelation = new UserMapRelation();
//        userMapRelation.setUser_id(user.getId());
//        userMapRelation.setMap_id(map.getId());
//        userMapRelation.setPass(true); //TODO
//        int result2 = mapMapper.insertUserMapRelation(userMapRelation);
        return map;
    }

    /**
     * 修改用户故事地图
     *
     * @param map         原地图
     * @param name        地图名称
     * @param description 地图描述
     * @return 地图列表
     */
    public Map modifyMap(Map map, String name, String description) {
        Map newmap = new Map();
        newmap.setMap_name(name);
        newmap.setDescription(description);
        newmap.setId(map.getId());
        mapMapper.update(newmap);
        return newmap;
    }

    /**
     * 删除地图
     *
     * @param map  地图
     * @param user 删除者
     */
    @Transactional
    public void deleteMap(Map map, User user) {
        if (map == null || user == null) {
            return;
        }

        if (map.getOwner_id() == user.getId()) { // owner
            mapMapper.deleteUserMapRelation(map.getId());
            mapMapper.delete(map.getId());
        } else {
            mapMapper.deleteUserMapRelationByUseridAndMapId(map.getId(), user.getId());
        }
    }

    // ====== edit by sunx ======

    /**
     * 邀请协作者
     *
     * @param ownerName 地图拥有者
     * @param coUserId  协作者
     * @param mapId     地图
     * @return 是否成功
     */
    public boolean inviteCollaborator(String ownerName, long coUserId, long mapId) {
        User owner = userMapper.find(ownerName);
        Map map = mapMapper.findByMapId(mapId);
        boolean success = true;

        if (owner != null && map != null && owner.getId() == map.getOwner_id()) {//owner
            UserMapRelation existedInv = mapMapper.findUserMapRelationByUseridAndMapId(mapId, coUserId);
            if (existedInv == null) {
                // the first time
                // new invitation
                UserMapRelation newInv = new UserMapRelation();
                newInv.setUser_id(coUserId);
                newInv.setMap_id(mapId);
                mapMapper.insertUserMapRelation(newInv);
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    /**
     * 删除协作者
     *
     * @param ownerName 邀请者
     * @param coUserId  被邀请者
     * @param mapId     地图
     * @return 结果
     */
    public boolean deleteInvitation(String ownerName, long coUserId, long mapId) {
        User owner = userMapper.find(ownerName);
        Map map = mapMapper.findByMapId(mapId);
        boolean success = true;
        if (owner != null && map != null && owner.getId() == map.getOwner_id()) {//owner
            mapMapper.deleteUserMapRelationByUseridAndMapId(mapId, coUserId);
        } else {
            success = false;
        }
        return success;
    }

    /**
     * 获取用户收到的所有协作邀请
     *
     * @param userId 用户
     * @return 邀请列表
     */
    public List<UserMapRelation> getReceivedInvitations(long userId, boolean newOnly) {
        if (newOnly) {
            // 没有响应的
            return mapMapper.getReceivedInvitations(userId, Constants.RESPONSE_WAIT)
                    .stream().map(usr -> {
                        usr.setMap(mapMapper.findByMapId(usr.getMap_id()));
                        return usr;
                    }).collect(Collectors.toList());
        } else {
            // 全部
            return mapMapper.getAllReceivedInvitations(userId)
                    .stream().map(usr -> {
                        usr.setMap(mapMapper.findByMapId(usr.getMap_id()));
                        return usr;
                    }).collect(Collectors.toList());
        }
    }

    /**
     * 获取当前用户为当前地图发送的所有邀请
     *
     * @param mapId   地图id
     * @param ownerId 当前用户id
     * @return 邀请列表
     */
    public List<UserMapRelation> getSentInvitations(long mapId, long ownerId) {
        Map map = mapMapper.findByMapId(mapId);
        if (map != null && map.getOwner_id() == ownerId) {
            return mapMapper.findUserMapRelationByMapId(mapId);
        } else {
            return null;
        }
    }

    /**
     * 获取地图所有协作者
     * 当前用户必须是地图的拥有者或协作者
     *
     * @param mapId        地图
     * @param containOwner 是否包含owner
     * @return 协作者列表
     */
    public List<User> getCollaborators(long mapId, String username, boolean containOwner) {
        List<User> collaborators = null;

        User currUser = userMapper.find(username);  // 当前查询用户
        Map map = mapMapper.findByMapId(mapId);     // 查询的地图

        if (currUser != null && map != null) {
            User owner = userMapper.findById(map.getOwner_id());    // map owner
            if (owner != null) {
                boolean currIsOwner = owner.getId() == currUser.getId();
                collaborators = mapMapper.getCollaborators(mapId);
                if (containOwner) {
                    collaborators.add(owner);
                }

                boolean containCurr = false;
                for (User c : collaborators) {
                    if (c.getId() == currUser.getId()) {
                        containCurr = true;
                    }
                }

                if (!currIsOwner && !containCurr) {
                    // 既不是拥有者，也不是参与者，无权查看
                    collaborators = null;
                }
            }
        }
        return collaborators;
    }


    /**
     * 响应邀请
     * 接收、拒绝、忽略
     *
     * @param username 用户
     * @param invId    邀请id
     * @param response 响应结果
     * @return 是否成功
     */
    public boolean responseInvitation(String username, long invId, int response) {
        User user = userMapper.find(username);
        UserMapRelation invitation = mapMapper.findInvitation(invId);
        boolean success = false;

        if (user != null &&
                invitation != null &&
                invitation.getUser_id() == user.getId() &&
                invitation.isResponsable()) {
            // 接受响应
            if (response == Constants.RESPONSE_REJECT ||
                    response == Constants.RESPONSE_ACCEPT || response == Constants.RESPONSE_IGNORE) {
                // 响应类别值合法
                invitation.setResponse(response);
                mapMapper.updateCollaboration(invitation);
                success = true;
            }
        }
        return success;
    }

}
