package edu.nju.usm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * 地图模型
 *
 * @author YUF
 * @date 2018/01/09
 */
@Data
@NoArgsConstructor
public class Map {

    private long id;
    private String map_name;
    private String description;
    private long owner_id;
    private Date created_time;

}
