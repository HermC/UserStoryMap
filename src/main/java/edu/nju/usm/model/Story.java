package edu.nju.usm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

/**
 * 故事模型
 *
 * @author YUF
 * @date 2018/01/09
 * */
@Data
@NoArgsConstructor
public class Story {
    enum StoryType{
        GOAL,STREAM,STORY;
    }
    enum StoryStatus{
        TODO,DOING,DONE;
    }
    private long id;
    private long map_id;
    private long release_id;
    private long created_user_id;
    private long parent_story_id;
    private StoryType story_type;
    private StoryStatus story_status;
    private Time created_time;
    private String description;
    private String story_name;

    public long getId() {
        return id;
    }

    public long getMap_id() {
        return map_id;
    }

    public void setMap_id(long map_id) {
        this.map_id = map_id;
    }

    public long getRelease_id() {
        return release_id;
    }

    public void setRelease_id(long release_id) {
        this.release_id = release_id;
    }

    public long getCreated_user_id() {
        return created_user_id;
    }

    public void setCreated_user_id(long created_user_id) {
        this.created_user_id = created_user_id;
    }

    public long getParent_story_id() {
        return parent_story_id;
    }

    public void setParent_story_id(long parent_story_id) {
        this.parent_story_id = parent_story_id;
    }

    public StoryType getStory_type() {
        return story_type;
    }

    public void setStory_type(StoryType story_type) {
        this.story_type = story_type;
    }

    public StoryStatus getStory_status() {
        return story_status;
    }

    public void setStory_status(StoryStatus story_status) {
        this.story_status = story_status;
    }

    public Time getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Time created_time) {
        this.created_time = created_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStory_name() {
        return story_name;
    }

    public void setStory_name(String story_name) {
        this.story_name = story_name;
    }
}
