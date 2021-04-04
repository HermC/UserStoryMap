package edu.nju.usm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

/**
 * 故事模型
 *
 * @author YUF
 * @date 2018/01/09
 */
@Data
@NoArgsConstructor
public class Story {

    public enum StoryType {
        GOAL, STREAM, STORY
    }

    public enum StoryStatus {
        TODO, DOING, DONE
    }

    private long id;
    private long map_id;
    private long release_id;
    private long created_user_id;
    private long parent_story_id;
    private StoryType story_type;
    private StoryStatus story_status;
    private Date created_time;
    private String description;
    private String story_name;

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", map_id=" + map_id +
                ", release_id=" + release_id +
                ", created_user_id=" + created_user_id +
                ", parent_story_id=" + parent_story_id +
                ", story_type=" + story_type +
                ", story_status=" + story_status +
                ", created_time=" + created_time +
                ", description='" + description + '\'' +
                ", story_name='" + story_name + '\'' +
                '}';
    }
}
