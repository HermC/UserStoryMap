package edu.nju.usm.mapper;

import edu.nju.usm.model.Map;
import edu.nju.usm.model.Story;
import edu.nju.usm.model.UserMapRelation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 故事模型数据库方法
 *
 * @author YUF
 * @date 2018/01/15
 */
@Repository
@Mapper
public interface StoryMapper {
    /**
     * 按story_id查询故事
     */
    @Select("SELECT * FROM story WHERE id = #{story_id}")
    public Story findByStoryId(@Param("story_id") final long story_id);

    /**
     * 按map_id查询故事列表
     */
    @Select("SELECT * FROM story WHERE map_id = #{map_id}")
    public List<Story> findByMapId(@Param("map_id") final long map_id);

    @Select("SELECT * FROM story WHERE map_id = #{map_id} and parent_story_id = #{parent_story_id}")
    public List<Story> findByMapIdAndParentId(@Param("map_id") final long map_id,
                                              @Param("parent_story_id") final long parentStoryId);

    /**
     * 新增故事
     */
    @Insert({"INSERT INTO story(map_id,created_user_id,story_type,story_status,description,story_name,parent_story_id,release_id) VALUES(#{map_id}, #{created_user_id}, #{story_type}, #{story_status}, #{description}, #{story_name},#{parent_story_id},#{release_id})"})
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int insert(Story story);

    /**
     * 更新故事名称 描述 状态 类型
     * <p>
     * 动态sql生成
     */
    @Update("<script>" +
            "UPDATE story SET " +
            "<if test='parent_story_id != null'>parent_story_id = #{parent_story_id}, </if>" +
            "<if test='release_id != null'>release_id = #{release_id}, </if>" +
            "<if test='story_type != null'>story_type = #{story_type}, </if>" +
            "<if test='story_status != null'>story_status = #{story_status}, </if>" +
            "<if test='description != null'>description = #{description}, </if>" +
            "<if test='story_name != null'>story_name = #{story_name} </if>" +
            "WHERE id = #{id}" +
            "</script>")
    public int update(Story story);

    /**
     * 更新故事 状态
     */
    @Update("UPDATE story SET story_status=#{story_status} WHERE id = #{id}")
    public int updateStatus(@Param("story_status") Story.StoryStatus story_status, @Param("id") long id);

    /**
     * 更新故事 类型
     */
    @Update("UPDATE story SET story_type=#{story_type} WHERE id = #{id}")
    public int updateType(@Param("story_type") Story.StoryType story_type, @Param("id") long id);

    /**
     * 更新故事 父故事id
     */
    @Update("UPDATE story SET parent_story_id=#{parent_story_id} WHERE id = #{id}")
    public int updateParent(@Param("parent_story_id") long parent_story_id, @Param("id") long id);

    /**
     * 更新故事 发布id
     */
    @Update("UPDATE story SET release_id=#{release_id} WHERE id = #{id}")
    public int updateRelease(@Param("release_id") long release_id, @Param("id") long id);

    /**
     * 删除故事
     */
    @Delete("DELETE FROM story WHERE id = #{story_id}")
    public int delete(@Param("story_id") final long story_id);

}
