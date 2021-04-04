package edu.nju.usm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户模型
 *
 * @author HermC yzy627@126.com
 * @date 2018/01/04
 * @time 09:44
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"handler"})    // 由于使用ibatis懒加载模式获取roles，会导致在json序列化的时候出错，因此忽略handler属性的序列化
public class User {

    private long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    private int ban;
    private List<String> roles;
    
}
