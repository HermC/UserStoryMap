package edu.nju.usm.command;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户故事地图元数据封装
 *
 * @author YUF
 * @date 2018/01/14
 */
@Data
@NoArgsConstructor
public class StoryCommand {

    private String story_name;
    private String description;
    private String story_type;
    private String story_status;
    private long map_id;
    private long parent_story_id;
    private long release_id;

}
