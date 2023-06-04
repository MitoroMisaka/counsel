package com.ecnu.rai.counsel.config;

import com.ecnu.rai.counsel.shiro.MyRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class ShiroConfig {

    @Bean
    public DefaultWebSecurityManager securityManager(MyRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(realm);
        log.info("securityManager -----------> 初始化了");
        return securityManager;
    }

    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/unauth");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauth");


        Map<String, String> hashMap = new LinkedHashMap<>();

        // shirofilter 过滤器，限制了页面的访问
        // anon表示不登陆也可以访问
        // role[*]表示相应的角色可以访问

        // swagger2
        hashMap.put("/swagger-ui.html", "anon");

        //account
        hashMap.put("/account/login", "anon");
        hashMap.put("/account/logout", "anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(hashMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 开启aop注解支持
     * 即在controller中使用 @RequiresPermissions("user/userList")
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        //设置安全管理器
        attributeSourceAdvisor.setSecurityManager(securityManager);
        return attributeSourceAdvisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }


    // shiro-redis
    //====== session共享 ========
    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1:6379");
        redisManager.setDatabase(0);
        return redisManager;
    }

    /**
     * DAO大家都用过，数据访问对象，用于会话的CRUD，比如我们想把Session保存到数据库，那么可以实现自己的SessionDAO，
     * 通过如JDBC写到数据库；比如想把Session放到Memcached中，可以实现自己的Memcached SessionDAO；
     * 另外SessionDAO中可以使用Cache进行缓存，以提高性能；
     */
    @Bean
    @ConfigurationProperties(prefix = "shiro.redis")
    RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    @Bean
    DefaultWebSessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    /**
     * 缓存控制器，来管理如用户、角色、权限等的缓存的；
     * 因为这些数据基本上很少去改变，放到缓存中后可以提高访问的性能
     */
    @Bean
    @ConfigurationProperties(prefix = "shiro.cache")
    RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
        redisCacheManager.setPrincipalIdFieldName("id");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }

}
