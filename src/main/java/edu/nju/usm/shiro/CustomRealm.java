package edu.nju.usm.shiro;

import edu.nju.usm.mapper.UserMapper;
import edu.nju.usm.model.User;
import edu.nju.usm.utils.JwtUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm
 *
 * @author HermC yzy627@126.com
 * @version 1.0
 * @date 2019/01/04
 * @time 22:10
 * */
@Component
public class CustomRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 当需要检测用户权限的时候调用此方法
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("---- 权限认证方法 ----");
        String token = (String) principalCollection.getPrimaryPrincipal();
        // 解密获得username，用于与数据库对比
        String username = jwtUtils.getUsername(token);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 获得该用户角色
        User user = userMapper.find(username);
        if (user == null) {
            throw new AuthorizationException("该用户不存在!");
        }
        // 设置该用户具有的角色
        Set<String> roles = new HashSet<>();
        System.out.println(user.getRoles().size());
        for (String s: user.getRoles()) {
            roles.add(s);
        }
        info.setRoles(roles);
        // TODO: 设置该用户具有的权限

        return info;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        System.out.println("---- 身份认证方法 ----");
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于与数据库对比
        String username = jwtUtils.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token认证失败!");
        }
        if (jwtUtils.verifyTimeout(token)) {
            throw new AuthenticationException("token已过期!");
        }
        if (!jwtUtils.verify(token, username)) {
            throw new AuthenticationException("token认证失败!");
        }
        User user = userMapper.find(username);
        if (user == null) {
            throw new AuthenticationException("用户不存在!");
        }
        int ban = user.getBan();
        if (ban == 1) {
            throw new AuthenticationException("用户已被封号!");
        }
        return new SimpleAuthenticationInfo(token, token, "CustomRealm");
    }
}
