package edu.nju.usm.service;

import edu.nju.usm.mapper.MapMapper;
import edu.nju.usm.mapper.RoleMapper;
import edu.nju.usm.mapper.StoryMapper;
import edu.nju.usm.mapper.UserMapper;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.Story;
import edu.nju.usm.model.User;
import edu.nju.usm.model.UserMapRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 故事信息处理
 *
 * @author YUF
 * @date 2018/01/15
 * */
@Service
public class StoryService {
    @Autowired
    private MapMapper mapMapper;
    @Autowired
    private StoryMapper storyMapper;

    /**
     * 通过地图ip查找故事列表
     *
     * @param map_id 地图id
     * @return 地图列表
     * */
    public List<Story> getStoryList(long map_id) {
        List<Story> storyList=storyMapper.findByMapId(map_id);
        System.out.println(storyList.toString());
        if(storyList==null||storyList.isEmpty()){
            return null;
        }
        return storyList;
    }

    /**
     * 通过故事id查找故事详情
     *
     * @param story_id 故事id
     * @return 地图
     * */
    public Story getStoryById(long story_id) {
        Story story=storyMapper.findByStoryId(story_id);
        return story;
    }

    /**
     * 创建故事
     *
     * @param name 故事名称
     * @param description 故事描述
     * @param map_id 地图id
     * @param parent_id 上级故事id
     * @param type 类型
     * @param user 创建者
     * @return 地图列表
     * */
    public Story createStory(String name,String description,long map_id,long parent_id,String type,String status,User user) {
        Story story=new Story();
        Map map=mapMapper.findByMapId(map_id);
        if(map==null){
            return null;
        }
        story.setMap_id(map_id);
        if(parent_id>-1) {
            Story parent = storyMapper.findByStoryId(parent_id);
            if(parent==null){
                return null;
            }
        }
        story.setParent_story_id(parent_id);
        story.setCreated_time(new java.sql.Date(new Date().getTime()));
        story.setCreated_user_id(user.getId());
        story.setDescription(description);
        story.setRelease_id(-1);
        story.setStory_name(name);
        switch(type){
            case "GOAL":
                story.setStory_type(Story.StoryType.GOAL);
                break;
            case "STREAM":
                story.setStory_type(Story.StoryType.STREAM);
                break;
            case "STORY":
                story.setStory_type(Story.StoryType.STORY);
                break;
            default:
                return null;
        }
        switch(status){
            case "TODO":
                story.setStory_status(Story.StoryStatus.TODO);
                break;
            case "DOING":
                story.setStory_status(Story.StoryStatus.DOING);
                break;
            case "DONE":
                story.setStory_status(Story.StoryStatus.DONE);
                break;
            default:
                return null;
        }
        story.setStory_status(Story.StoryStatus.TODO);
        int id=storyMapper.insert(story);
        return story;
    }

    /**
     * 修改故事
     *
     * @param story_id 故事id
     * @param name 故事名称
     * @param description 故事描述
     * @param type 故事类型
     * @param status 故事状态
     * @return 新故事
     * */
    public Story modifyStory(long story_id,String name,String description,String type,String status) {
        Story newStory=new Story();
        newStory.setId(story_id);
        newStory.setStory_name(name);
        newStory.setDescription(description);
        switch(type){
            case "GOAL":
                newStory.setStory_type(Story.StoryType.GOAL);
                break;
            case "STREAM":
                newStory.setStory_type(Story.StoryType.STREAM);
                break;
            case "STORY":
                newStory.setStory_type(Story.StoryType.STORY);
                break;
            default:
                return null;
        }
        switch(status){
            case "TODO":
                newStory.setStory_status(Story.StoryStatus.TODO);
                break;
            case "DOING":
                newStory.setStory_status(Story.StoryStatus.DOING);
                break;
            case "DONE":
                newStory.setStory_status(Story.StoryStatus.DONE);
                break;
            default:
                return null;
        }
        int result=storyMapper.updateTypeStatusNameDes(newStory);
        if(result!=1){
            return null;
        }
        return newStory;
    }
    /**
     * 修改故事类型
     *
     * @param story_id 故事id
     * @param type 故事类型
     * @return 新故事
     * */
    public Story modifyStoryType(long story_id,String type) {
        int result=1;
        switch(type){
            case "GOAL":
                result=storyMapper.updateType(Story.StoryType.GOAL,story_id);
                break;
            case "STREAM":
                result=storyMapper.updateType(Story.StoryType.STREAM,story_id);
                break;
            case "STORY":
                result=storyMapper.updateType(Story.StoryType.STORY,story_id);
                break;
            default:
                return null;
        }
        if(result!=1){
            return null;
        }
        Story newStory=storyMapper.findByStoryId(story_id);
        return newStory;
    }
    /**
     * 修改故事状态
     *
     * @param story_id 故事id
     * @param status 故事状态
     * @return 新故事
     * */
    public Story modifyStoryStatus(long story_id,String status) {
        int result=1;
        switch(status){
            case "TODO":
                result=storyMapper.updateStatus(Story.StoryStatus.TODO,story_id);
                break;
            case "DOING":
                result=storyMapper.updateStatus(Story.StoryStatus.DOING,story_id);
                break;
            case "DONE":
                result=storyMapper.updateStatus(Story.StoryStatus.DONE,story_id);
                break;
            default:
                return null;
        }
        if(result!=1){
            return null;
        }
        Story newStory=storyMapper.findByStoryId(story_id);
        return newStory;
    }
    /**
     * 修改故事状态
     *
     * @param story_id 故事id
     * @param release_id 发布id
     * @return 新故事
     * */
    public Story modifyStoryRelease(long story_id,long release_id) {
        int result=storyMapper.updateRelease(release_id,story_id);
        if(result!=1){
            return null;
        }
        Story newStory=storyMapper.findByStoryId(story_id);
        return newStory;
    }
    /**
     * 修改故事 父故事
     *
     * @param story_id 故事id
     * @param parent_id 父故事id
     * @return 新故事
     * */
    public Story modifyStoryParent(long story_id,long parent_id) {
        Story parent=storyMapper.findByStoryId(parent_id);
        if(parent==null){
            return null;
        }
        int result=storyMapper.updateParent(parent_id,story_id);
        if(result!=1){
            return null;
        }
        Story newStory=storyMapper.findByStoryId(story_id);
        return newStory;
    }
    /**
     * 删除故事
     *
     * @param story_id 故事id
     * */
    public void deleteStory(long story_id) {
        storyMapper.delete(story_id);
    }
}
