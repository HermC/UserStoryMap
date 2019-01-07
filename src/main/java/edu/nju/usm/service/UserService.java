package edu.nju.usm.service;

import edu.nju.usm.mapper.RoleMapper;
import edu.nju.usm.mapper.UserMapper;
import edu.nju.usm.model.User;
import edu.nju.usm.utils.Constants;
import edu.nju.usm.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 通过用户名查找用户信息
     *
     * @param username 用户名
     * @return 用户信息
     * */
    public User getUser(String username) {
        if (username == null) {
            return null;
        }
        return userMapper.find(username);
    }

    /**
     * 根据用户名判断用户是否存在
     *
     * @param username 用户名
     * @return true用户存在，false用户不存在
     * */
    public boolean isExisted(String username) {
        return this.getUser(username) != null;
    }

    /**
     * 新增用户，用户需要提供用户名、密码和邮箱
     * 建议在使用前检查用户是否存在
     *
     * @param user 用户提供信息
     * @return 新增行数。如果添加用户成功，一般会返回1；如果用户存在，以及其他一些错误，会返回0
     * */
    @Transactional
    public int addUser(User user) {
        if (user == null) {
            return 0;
        }
        // 默认用户需要填写username, password和email
        if (user.getUsername() == null
                || user.getPassword() == null
                || user.getEmail() == null) {
            return 0;
        }
        if (this.getUser(user.getUsername()) != null) {
            return 0;
        }
        String salt = ShiroUtils.generateSalt(Constants.SALT_LENGTH);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.encryptPassword(Constants.MD5, user.getPassword(), user.getSalt()));
        user.setBan(Constants.NOT_BAN);

        int rows = userMapper.insert(user);
        roleMapper.insert(user.getId(), Constants.ROLE_USER);

        List<String> roles = new ArrayList<>();
        roles.add(Constants.ROLE_USER);
        user.setRoles(roles);

        return rows;
    }

    /**
     * 修改用户基本信息，密码、权限不在此处修改
     *
     * @param user 用户更新信息
     * @return 更新行数。如果更新成功，一般会返回1；如果存在错误，会返回0
     * */
    public int modifyUserInfo(User user) {
        if (user == null) {
            return 0;
        }
        if (user.getUsername() == null) {
            return 0;
        }
        User oldUser = userMapper.find(user.getUsername());
        if (oldUser == null) {
            return 0;
        }
        oldUser.setEmail(user.getEmail());
        // TODO: 其他可能新增的用户属性更新

        return userMapper.update(oldUser);
    }

}
