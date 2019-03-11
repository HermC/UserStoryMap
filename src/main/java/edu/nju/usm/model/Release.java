package edu.nju.usm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

}
