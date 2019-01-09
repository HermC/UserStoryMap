package edu.nju.usm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * 用户和地图对应关系模型
 *
 * @author YUF
 * @date 2018/01/09
 * */
@Data
@NoArgsConstructor
public class UserMapRelation {
    private long id;
    private long user_id;
    private long map_id;
    private boolean pass;

    public long getId() {
        return id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getMap_id() {
        return map_id;
    }

    public void setMap_id(long map_id) {
        this.map_id = map_id;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }
}
