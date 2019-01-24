package edu.nju.usm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

/**
 * 发布模型
 *
 * @author YUF
 * @date 2018/01/09
 */
@Data
@NoArgsConstructor
public class Release {

    private long id;
    private long map_id;
    private String release_name;
    private Date created_time;
    private Date deadline;

}
