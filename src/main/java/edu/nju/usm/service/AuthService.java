package edu.nju.usm.service;

import edu.nju.usm.mapper.UserMapper;
import edu.nju.usm.model.User;
import edu.nju.usm.utils.Constants;
import edu.nju.usm.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 验证用户是否存在
     *
     * @param username 用户名
     * @return true用户存在，false用户不存在
     * */
    public boolean verifyUser(String username) {
        return username != null && userMapper.find(username) != null;
    }

    /**
     * 验证用户密码是否正确
     *
     * @param username 用户名
     * @param password 密码
     * @return true密码正确，false密码不正确
     * */
    public boolean verifyPassword(String username, String password) {
        if (username == null || username.equals("")) {
            return false;
        }
        User user = userMapper.find(username);
        if (user == null) {
            return false;
        }
        password = ShiroUtils.encryptPassword(Constants.MD5, password, user.getSalt());
        return password.equals(user.getPassword());
    }

    /**
     * 修改密码
     *
     * @param username 用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 更新数据库行数
     * */
    public int modifyPassword(String username, String oldPassword, String newPassword) {
        if (username == null || username.equals("")) {
            return 0;
        }
        User user = userMapper.find(username);
        if (user == null) {
            return 0;
        }
        if (oldPassword == null || newPassword == null) {
            return 0;
        }
        oldPassword = ShiroUtils.encryptPassword(Constants.MD5, oldPassword, user.getSalt());
        if (!oldPassword.equals(user.getPassword())) {
            return 0;
        }
        // 更新生成加密salt
        user.setSalt(ShiroUtils.generateSalt(Constants.SALT_LENGTH));
        newPassword = ShiroUtils.encryptPassword(Constants.MD5, newPassword, user.getSalt());
        user.setPassword(newPassword);
        return userMapper.update(user);
    }

}
