package edu.nju.usm.config;

import edu.nju.usm.intercepter.JwtFilter;
import edu.nju.usm.shiro.CustomRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Shiro权限验证的配置方法，设置Shiro的SecurityManager和Filter
 *
 * @author HermC yzy627@126.com
 * @version 1.0
 * @date 2019/01/06
 * @time 22:11
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean factory(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器
        Map<String, Filter> filters = new HashMap<>();
        // 设置自定义Jwt过滤器
        filters.put("jwt", new JwtFilter());
        factoryBean.setFilters(filters);
        factoryBean.setSecurityManager(securityManager);

        // 设置无权限时跳转的 url
        factoryBean.setUnauthorizedUrl("/unauthorized");

        Map<String, String> ruleMap = new HashMap<>();
        // 所有请求通过我们自己的JWT Filter
        ruleMap.put("/**", "anon");



        // 访问 /auth/token 权限获取路径不通过JwtFilter
        ruleMap.put("/auth/token", "anon");
        // 访问 /user/register 用户注册路径不通过JwtFilter
        ruleMap.put("/user/register", "anon");
        // 访问 /unauthorized/** 不通过JwtFilter
        ruleMap.put("/unauthorized/**", "anon");

        ruleMap.put("/auth/**", "jwt");
        ruleMap.put("/user/**", "jwt");
        ruleMap.put("/map/**", "jwt");
        ruleMap.put("/story/**", "jwt");
        ruleMap.put("/release/**", "jwt");

        factoryBean.setFilterChainDefinitionMap(ruleMap);

        return factoryBean;
    }

    /**
     * 注入SecurityManager
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(CustomRealm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置自定义Realm
        securityManager.setRealm(customRealm);

        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * 添加注解支持
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
