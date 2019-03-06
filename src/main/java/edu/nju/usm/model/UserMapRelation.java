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

    // 邀请时间
    private Date start_time;

    // 被邀请者响应结果
    // wait=0;accept=1;reject=-1
    private int response;


    /**
     * 邀请是否被接受且有效
     *
     * 满足两个条件：
     * 1. 被对方接受
     * 2. 在有效期内
     * @return 是否被接受且有效
     */
    public boolean isAccepted(){
        return response == Constants.RESPONSE_ACCEPT;
    }



    /**
     * 是否接受响应
     * 未过期且未响应过即可接受
     *
     * @return 是否接受
     */
    public boolean isResponsable(){
        return response == Constants.RESPONSE_WAIT;
    }
}
