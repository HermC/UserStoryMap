package edu.nju.usm.mapper;

import edu.nju.usm.model.Map;
import edu.nju.usm.model.User;
import edu.nju.usm.model.UserMapRelation;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 地图模型数据库方法
 *
 * @author YUF
 * @date 2018/01/09
 *
 * modified by sunx
 */
@Repository
@Mapper
public interface MapMapper {
    /**
     * 按user_id查询可见的所有地图
     */
    @Select("SELECT map_id FROM user_map_relation WHERE user_id = #{user_id} AND response = 1 AND end_time IS NOT NULL" +
            " UNION " +
            "SELECT id FROM map WHERE owner_id = #{user_id}")
    public List<Long> findByUserId(@Param("user_id") final long user_id);


    @Select("SELECT * FROM map WHERE owner_id = #{user_id} and id = #{map_id}")
    public Map findByUserIdMapId(@Param("user_id") final long user_id,
                                 @Param("map_id") final long map_id);

    /**
     * 按map_id查询地图
     */
    @Select("SELECT * FROM map WHERE id = #{map_id}")
    public Map findByMapId(@Param("map_id") final long map_id);

    /**
     * 按map_id查询地图对应列表
     */
    @Select("SELECT * FROM user_map_relation WHERE map_id = #{map_id}")
    public List<UserMapRelation> findUserMapRelationByMapId(@Param("map_id") final long map_id);

    /**
     * 按user_id map_id查询地图对应列表
     */
    @Select("SELECT * FROM user_map_relation WHERE map_id = #{map_id} and user_id=#{user_id}")
    public UserMapRelation findUserMapRelationByUseridAndMapId(@Param("map_id") final long map_id, @Param("user_id") final long user_id);

    /**
     * 新增地图
     */
    @Insert({"INSERT INTO map(map_name, description, owner_id) VALUES(#{map_name}, #{description}, #{owner_id})"})
    @Options(useGeneratedKeys = true)
    public int insert(Map map);

    /**
     * 新增用户地图关系
     */
    @Insert({"INSERT INTO user_map_relation(map_id,user_id) VALUES(#{map_id}, #{user_id})"})
    @Options(useGeneratedKeys = true)
    public int insertUserMapRelation(UserMapRelation userMapRelation);

    /**
     * 更新地图
     * <p>
     * 动态sql生成
     */
    @Update("<script>" +
            "UPDATE map SET " +
            "<if test='map_name != null'>map_name = #{map_name}, </if>" +
            "<if test='description != null'>description = #{description} </if>" +
            "WHERE id = #{id}" +
            "</script>")
    public int update(Map map);

    /**
     * 删除地图
     */
    @Delete("DELETE FROM map WHERE id = #{map_id}")
    public int delete(@Param("map_id") final long map_id);

    /**
     * 删除地图和用户的对应
     */
    @Delete("DELETE FROM user_map_relation WHERE map_id = #{map_id} and user_id=#{user_id}")
    public int deleteUserMapRelationByUseridAndMapId(@Param("map_id") final long map_id, @Param("user_id") final long user_id);

    /**
     * 删除地图和用户的对应
     */
    @Delete("DELETE FROM user_map_relation WHERE map_id = #{map_id}")
    public int deleteUserMapRelation(@Param("map_id") final long map_id);
}
