package edu.nju.usm.command;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户故事地图元数据封装
 *
 * @author YUF
 * @date 2018/01/14
 * */
@Data
@NoArgsConstructor
public class StoryCommand {

    private String story_name;
    private String description;
    private String story_type;
    private String story_status;
    private long map_id;
    private long parent_story_id;

    public String getStory_name() {
        return story_name;
    }

    public void setStory_name(String story_name) {
        this.story_name = story_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStory_type() {
        return story_type;
    }

    public void setStory_type(String story_type) {
        this.story_type = story_type;
    }

    public String getStory_status() {
        return story_status;
    }

    public void setStory_status(String story_status) {
        this.story_status = story_status;
    }

    public long getMap_id() {
        return map_id;
    }

    public void setMap_id(long map_id) {
        this.map_id = map_id;
    }

    public long getParent_story_id() {
        return parent_story_id;
    }

    public void setParent_story_id(long parent_story_id) {
        this.parent_story_id = parent_story_id;
    }
}
