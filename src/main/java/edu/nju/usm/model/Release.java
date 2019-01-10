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
 * */
@Data
@NoArgsConstructor
public class Release {

    private long id;
    private long map_id;
    private String release_name;
    private Time created_time;
    private Time deadline;

    public long getId() {
        return id;
    }

    public long getMap_id() {
        return map_id;
    }

    public void setMap_id(long map_id) {
        this.map_id = map_id;
    }

    public String getRelease_name() {
        return release_name;
    }

    public void setRelease_name(String release_name) {
        this.release_name = release_name;
    }

    public Time getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Time created_time) {
        this.created_time = created_time;
    }

    public Time getDeadline() {
        return deadline;
    }

    public void setDeadline(Time deadline) {
        this.deadline = deadline;
    }
}
