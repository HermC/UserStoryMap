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
 * */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = { "handler" })    // 由于使用ibatis懒加载模式获取roles，会导致在json序列化的时候出错，因此忽略handler属性的序列化
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

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getBan() {
        return ban;
    }

    public void setBan(int ban) {
        this.ban = ban;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
