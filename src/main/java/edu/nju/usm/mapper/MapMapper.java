package edu.nju.usm.mapper;

import edu.nju.usm.model.Map;
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
 * */
@Repository
@Mapper
public interface MapMapper {
    /**
     * 按user_id查询可见的所有地图
     * */
    @Select("SELECT map_id FROM user_map_relation WHERE user_id = #{user_id} and pass=true")
    public List<Long> findByUserId(@Param("user_id") final long user_id);

    @Select("SELECT * FROM map WHERE id = #{map_id}")
    public Map findByMapId(@Param("map_id") final long map_id);
}
