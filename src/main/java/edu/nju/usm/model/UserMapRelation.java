package edu.nju.usm.model;

import edu.nju.usm.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户与地图的协作关系
 *
 * @author YUF
 * @date 2018/01/09
 *
 * modifyed by sunx
 */
@Data
@NoArgsConstructor
public class UserMapRelation {
    private long id;
    // 被邀请者
    private long user_id;
    // 被邀请参与的map
    private long map_id;
    // TODO: 待删除
    private boolean pass;
    // 邀请时间
    private Date start_time;
    // 协作关系失效时间
    private Date end_time;
    // 被邀请者响应结果
    // wait=0;accept=1;reject=-1
    private int response;


    /**
     * 协作关系是否有效
     * 满足两个条件：
     * 1. 被对方接受
     * 2. 在有效期内
     * @return 是否有效
     */
    public boolean isAlive(){
        boolean accepted = response == Constants.RESPONSE_ACCEPT;
        boolean overdue = (end_time != null) && (new Date().before(end_time));
        return accepted && (!overdue);
    }
}
