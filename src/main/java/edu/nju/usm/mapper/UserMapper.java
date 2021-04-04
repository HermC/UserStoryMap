package edu.nju.usm.mapper;

import edu.nju.usm.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户模型数据库方法
 *
 * @author HermC yzy627@126.com
 * @date 2018/01/04
 * @time 09:44
 */
@Repository
@Mapper
public interface UserMapper {

    /**
     * 按username查询单个
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    @Results({
            @Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER),
            @Result(property = "username", column = "username", jdbcType = JdbcType.VARCHAR),
            @Result(property = "email", column = "email", jdbcType = JdbcType.VARCHAR),
            @Result(property = "password", column = "password", jdbcType = JdbcType.VARCHAR),
            @Result(property = "salt", column = "salt", jdbcType = JdbcType.VARCHAR),
            @Result(property = "ban", column = "ban", jdbcType = JdbcType.INTEGER),
            @Result(property = "roles", javaType = List.class,
                    column = "id", many = @Many(
                    select = "edu.nju.usm.mapper.RoleMapper.find",
                    fetchType = FetchType.LAZY
            ))
    })
    public User find(@Param("username") final String username);


    @Select("SELECT * FROM user WHERE id = #{user_id}")
    public User findById(@Param("user_id") final long userId);

    /**
     * 根据用户名模糊查询
     * @param uname 查询字符串
     * @return 用户名中包含查询字符串的一组用户
     */
    @Select("SELECT * FROM user WHERE username LIKE concat('%', #{uname}, '%') " +
            "ORDER BY username ASC")
    public List<User> searchByName(@Param("uname") final String uname);


    /**
     * 新增用户
     */
    @Insert({"INSERT INTO user(username, email, password, salt, ban) VALUES(#{username}, #{email}, #{password}, #{salt},#{ban})"})
    @Options(useGeneratedKeys = true)
    public int insert(User user);

    /**
     * 更新用户信息
     * <p>
     * 动态sql生成
     */
    @Update("<script>" +
            "UPDATE user SET " +
            "<if test='email != null'>email = #{email}, </if>" +
            "<if test='password != null'>password = #{password}, </if>" +
            "<if test='salt != null'>salt = #{salt}, </if>" +
            "ban = #{ban}, " +
            "updated_at = now() WHERE username = #{username}" +
            "</script>")
    public int update(User user);

    /**
     * 删除用户信息
     */
    @Delete("DELETE FROM user WHERE username = #{username}")
    public int delete(@Param("username") final String username);

}
