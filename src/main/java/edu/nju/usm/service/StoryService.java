package edu.nju.usm.service;

import edu.nju.usm.command.StoryCommand;
import edu.nju.usm.mapper.MapMapper;
import edu.nju.usm.mapper.StoryMapper;
import edu.nju.usm.model.Map;
import edu.nju.usm.model.Story;
import edu.nju.usm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 故事信息处理
 *
 * @author YUF
 * @date 2018/01/15
 */
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
     */
    public List<Story> getStoryList(long map_id) {
        List<Story> storyList = storyMapper.findByMapId(map_id);
        if (storyList == null || storyList.isEmpty()) {
            return null;
        }
        return storyList;
    }

    /**
     * 通过故事id查找故事详情
     *
     * @param story_id 故事id
     * @return 地图
     */
    public Story getStoryById(long story_id) {
        Story story = storyMapper.findByStoryId(story_id);
        return story;
    }

    /**
     * 创建故事
     *
     * @param name        故事名称
     * @param description 故事描述
     * @param map_id      地图id
     * @param parent_id   上级故事id
     * @param type        类型
     * @param user        创建者
     * @return 地图列表
     */
    public Story createStory(String name, String description, long map_id, long parent_id, String type, String status, User user) {
        Story story = new Story();
        Map map = mapMapper.findByMapId(map_id);
        if (map == null) {
            return null;
        }
        story.setMap_id(map_id);
        if (parent_id > -1) {
            Story parent = storyMapper.findByStoryId(parent_id);
            if (parent == null) {
                return null;
            }
        }
        story.setParent_story_id(parent_id);
        story.setCreated_time(new java.sql.Date(new Date().getTime()));
        story.setCreated_user_id(user.getId());
        story.setDescription(description);
        story.setRelease_id(-1);
        story.setStory_name(name);
        Story.StoryType sType = this.getType(type);
        if (sType == null) {
            return null;
        }
        story.setStory_type(sType);
        // 默认新建的故事就TODO的吧
        story.setStory_status(Story.StoryStatus.TODO);
        int id = storyMapper.insert(story);
        return story;
    }

    /**
     * 修改故事
     *
     * @param story_id 故事ID
     * @param command  故事信息封装
     * @return 新故事
     */
    public Story modifyStory(long story_id, StoryCommand command) {
        Story newStory = storyMapper.findByStoryId(story_id);
        if (newStory == null) {
            return null;
        }
        newStory.setParent_story_id(command.getParent_story_id());
        newStory.setRelease_id(command.getRelease_id());
        newStory.setStory_name(command.getStory_name());
        newStory.setDescription(command.getDescription());
        Story.StoryType sType = this.getType(command.getStory_type());
        if (sType == null) {
            return null;
        }
        newStory.setStory_type(sType);
        Story.StoryStatus sStatus = this.getStatus(command.getStory_status());
        if (sStatus == null) {
            return null;
        }
        newStory.setStory_status(sStatus);
        int result = storyMapper.update(newStory);
        if (result != 1) {
            return null;
        }
        return newStory;
    }

    /**
     * 修改故事类型
     *
     * @param story_id 故事id
     * @param type     故事类型
     * @return 新故事
     */
    public Story modifyStoryType(long story_id, String type) {
        int result = 1;
        Story.StoryType sType = this.getType(type);
        if (sType == null) {
            return null;
        }
        result = storyMapper.updateType(sType, story_id);
        if (result != 1) {
            return null;
        }
        Story newStory = storyMapper.findByStoryId(story_id);
        return newStory;
    }

    /**
     * 修改故事状态
     *
     * @param story_id 故事id
     * @param status   故事状态
     * @return 新故事
     */
    public Story modifyStoryStatus(long story_id, String status) {
        int result = 1;
        Story.StoryStatus sStatus = this.getStatus(status);
        if (sStatus == null) {
            return null;
        }
        result = storyMapper.updateStatus(sStatus, story_id);
        if (result != 1) {
            return null;
        }
        Story newStory = storyMapper.findByStoryId(story_id);
        return newStory;
    }

    /**
     * 修改故事release
     *
     * @param story_id   故事id
     * @param release_id 发布id
     * @return 新故事
     */
    public Story modifyStoryRelease(long story_id, long release_id) {
        int result = storyMapper.updateRelease(release_id, story_id);
        if (result != 1) {
            return null;
        }
        Story newStory = storyMapper.findByStoryId(story_id);
        return newStory;
    }

    /**
     * 修改故事 父故事
     *
     * @param story_id  故事id
     * @param parent_id 父故事id
     * @return 新故事
     */
    public Story modifyStoryParent(long story_id, long parent_id) {
        Story parent = storyMapper.findByStoryId(parent_id);
        if (parent == null) {
            return null;
        }
        int result = storyMapper.updateParent(parent_id, story_id);
        if (result != 1) {
            return null;
        }
        Story newStory = storyMapper.findByStoryId(story_id);
        return newStory;
    }

    /**
     * 删除故事 迭代删除关联的故事 仅会删除纵列的 移动横列的
     *
     * @param story_id 故事id
     */
    @Transactional
    public int deleteStory(long story_id) {
        Story targetStory = storyMapper.findByStoryId(story_id);
        List<Story> relatedStorys = storyMapper.findByMapIdAndParentId(targetStory.getMap_id(), targetStory.getId());
        for (Story s: relatedStorys) {
            if (targetStory.getStory_type() == Story.StoryType.GOAL) {
                if (s.getStory_type() == Story.StoryType.GOAL) {
                    // 这样说明是同一级的，只需要往前挪一个
                    s.setParent_story_id(targetStory.getParent_story_id());
                    storyMapper.update(s);
                } else if (s.getStory_type() == Story.StoryType.STREAM) {
                    this.deleteRecursive(s.getId());
                }
            } else if (targetStory.getStory_type() == Story.StoryType.STREAM) {
                if (s.getStory_type() == Story.StoryType.STREAM) {
                    // 这样说明是同一级的，只需要往前挪一个
                    s.setParent_story_id(targetStory.getParent_story_id());
                    storyMapper.update(s);
                } else if (s.getStory_type() == Story.StoryType.STORY) {
                    this.deleteRecursive(s.getId());
                }
            } else if (targetStory.getStory_type() == Story.StoryType.STORY) {
                if (s.getStory_type() == Story.StoryType.STORY) {
                    // 这样说明是同一级的，只需要往前挪一个
                    s.setParent_story_id(targetStory.getParent_story_id());
                    storyMapper.update(s);
                }
            }
        }
        int rows = storyMapper.delete(story_id);
        return rows;
    }

    /**
     * 递归删除相关故事 横列和纵列都会被删除
     *
     * @param story_id 故事id
     */
    @Transactional
    protected int deleteRecursive(long story_id) {
        Story targetStory = storyMapper.findByStoryId(story_id);
        List<Story> relatedStorys = new ArrayList<>();
        relatedStorys.add(targetStory);
        int i = 0;
        int rows = 0;
        while (i < relatedStorys.size()) {
            Story ts = relatedStorys.get(i);
            List<Story> tr = storyMapper.findByMapIdAndParentId(ts.getMap_id(), ts.getId());
            relatedStorys.addAll(tr);
            rows += storyMapper.delete(ts.getId());
            i += 1;
        }
        return rows;
    }


    private Story.StoryType getType(String type) {
        switch (type) {
            case "GOAL":
                return Story.StoryType.GOAL;
            case "STREAM":
                return Story.StoryType.STREAM;
            case "STORY":
                return Story.StoryType.STORY;
            default:
                return null;
        }
    }

    private Story.StoryStatus getStatus(String status) {
        switch (status) {
            case "TODO":
                return Story.StoryStatus.TODO;
            case "DOING":
                return Story.StoryStatus.DOING;
            case "DONE":
                return Story.StoryStatus.DONE;
            default:
                return null;
        }
    }
}
