package edu.nju.usm.mapper;

import edu.nju.usm.model.Release;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReleaseMapper {

    /**
     * 根据id查找release
     *
     * @param release_id id
     * @return release具体信息
     */
    @Select("SELECT * FROM `release` WHERE id = #{release_id} ORDER BY deadline ASC")
    public Release findByReleaseId(@Param("release_id") final long release_id);

    /**
     * 根据map_id查找release
     *
     * @param map_id 所属地图id
     * @return release具体信息列表
     */
    @Select("SELECT * FROM `release` WHERE map_id = #{map_id}")
    public List<Release> findByMapId(@Param("map_id") final long map_id);

    /**
     * 新增release
     *
     * @param release 具体信息
     * @return 插入行数
     */
    @Insert({"INSERT INTO `release` (map_id, release_name, deadline) VALUES(#{map_id}, #{release_name}, #{deadline})"})
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int insert(Release release);

    /**
     * 修改release
     *
     * @param release 具体信息
     * @return 修改行数
     */
    @Update("<script>" +
            "UPDATE `release` SET " +
            "<if test='release_name != null'>release_name = #{release_name}, </if>" +
            "<if test='deadline != null'>deadline = #{deadline} </if>" +
            "WHERE id = #{id}" +
            "</script>")
    public int update(Release release);

    /**
     * 删除对应release
     *
     * @param release_id id
     * @return 删除行数
     */
    @Update("DELETE FROM `release` WHERE id = #{id}")
    public int delete(@Param("id") final long release_id);

}
