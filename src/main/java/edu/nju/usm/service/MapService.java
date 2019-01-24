package edu.nju.usm.service;

import edu.nju.usm.mapper.MapMapper;
import edu.nju.usm.mapper.RoleMapper;
import edu.nju.usm.mapper.UserMapper;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.User;
import edu.nju.usm.model.UserMapRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    /**
     * 通过用户id查找地图列表
     *
     * @param user_id 用户id
     * @return 地图列表
     */
    public List<Map> getMapList(long user_id) {
        List<Long> mapIdList = mapMapper.findByUserId(user_id);
        List<Map> mapList = new ArrayList<>();
        for (int i = 0; i < mapIdList.size(); i++) {
            long mapId = mapIdList.get(i);
            mapList.add(mapMapper.findByMapId(mapId));
        }
        return mapList;
    }

    /**
     * 通过用户id和地图id查找地图
     *
     * @param user_id 用户id
     * @param map_id  地图id
     * @return 地图
     */
    public Map getMapByUserIdMapId(long user_id, long map_id) {
        UserMapRelation userMapRelation = mapMapper.findUserMapRelationByUseridAndMapId(map_id, user_id);

        if (userMapRelation != null && userMapRelation.isPass()) {
            Map map = mapMapper.findByMapId(map_id);
            return map;
        } else {
            return null;
        }
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
        UserMapRelation userMapRelation = new UserMapRelation();
        userMapRelation.setUser_id(user.getId());
        userMapRelation.setMap_id(map.getId());
        userMapRelation.setPass(true); //TODO
        int result2 = mapMapper.insertUserMapRelation(userMapRelation);
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
        if (map.getOwner_id() == user.getId()) { // owner
            mapMapper.deleteUserMapRelation(map.getId());
            mapMapper.delete(map.getId());
        } else {
            mapMapper.deleteUserMapRelationByUseridAndMapId(map.getId(), user.getId());
        }
    }
}
