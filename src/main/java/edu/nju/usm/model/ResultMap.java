package edu.nju.usm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口返回json数据的封装格式
 *
 * @author HermC yzy627@126.com
 * @version 1.0
 * @date 2019/01/06
 * @time 22:11
 */
@Data
@NoArgsConstructor
public class ResultMap {

    private int code = 200;
    private String message = "";
    private boolean success = true;
    private Map<String, Object> data = new HashMap<>();

    public ResultMap success() {
        this.success = true;
        return this;
    }

    public ResultMap data(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public ResultMap code(int code) {
        this.code = code;
        return this;
    }

    public ResultMap fail() {
        this.success = false;
        return this;
    }

    public ResultMap message(String message) {
        this.message = message;
        return this;
    }

}
