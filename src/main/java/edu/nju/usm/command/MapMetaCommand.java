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
public class MapMetaCommand {

    private String name;
    private String description;

}
