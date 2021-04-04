package edu.nju.usm.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色数据库方法
 *
 * @author HermC yzy627@126.com
 * @date 2018/01/05
 * @time 09:44
 */
@Repository
@Mapper
public interface RoleMapper {

    /**
     * 查找用户角色
     */
    @Select("SELECT role FROM role WHERE userid = #{id}")
    public List<String> find(@Param("id") final long id);

    /**
     * 新增用户角色
     */
    @Insert("INSERT INTO role VALUES(#{userid}, #{role})")
    public int insert(@Param("userid") final long userid,
                      @Param("role") final String role);

    /**
     * 删除用户角色
     */
    @Delete("DELETE FROM role WHERE userid = #{userid} AND role = #{role}")
    public int delete(@Param("userid") final long userid,
                      @Param("role") final String role);

}
