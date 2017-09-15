package com.drore.shiro;

import com.beust.jcommander.internal.Maps;
import com.drore.util.RedisProperties;
import com.drore.util.ShiroProperties;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: spring-boot-base <br/>
 * 创建日期:  2017/4/19 17:31  <br/>
 * 作者:    wdz
 */
@Configuration
public class ShiroConfig {
    /**
     * 此处核心，对多个filte进行管理，其它filer设置不自动加载，作为过滤链条使用
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        filterRegistration.setOrder(1);
        return filterRegistration;
    }





    /**
     * @return
     * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        bean.setLoginUrl("/login");
        bean.setUnauthorizedUrl("redirect:/unauthor");

        Map<String, Filter> filters = Maps.newHashMap();
        filters.put("sessionFilter", sessionFilter());
        filters.put("kickout", kickoutSessionControlFilter());
        filters.put("authc", urlPermissionsFilter());
        filters.put("anon", new AnonymousFilter());
        bean.setFilters(filters);

        Map<String, String> chains = Maps.newLinkedHashMap();
        chains.put("/favicon.ico", "anon");
        chains.put("/static/**", "anon");
        chains.put("/*.js", "anon");
        chains.put("/*.html", "anon");
        chains.put("/webjars/**", "anon");
        chains.put("/v2/api-docs", "anon");//swagger前端页面集成
        chains.put("/doc.html", "anon");//接口页面
        chains.put("/login", "anon");//登录页面
        chains.put("/user/loginCheck", "anon");//登录校验
        chains.put("/orderInfo/aliPay/notifyUrl", "anon");//支付宝支付回调函数
        chains.put("/orderInfo/aliPay/refundNotifyUrl", "anon");//支付宝支付回调函数
        chains.put("/servlet/captchaCode", "anon");
        chains.put("/unauthor", "anon");//未授权页面
//        chains.put("/user/logout", "anon");
        chains.put("/**", "anon");//
      //  chains.put("/**", "sessionFilter,kickout,user,authc");//
        bean.setFilterChainDefinitionMap(chains);
        return bean;
    }

    /**
     * @return
     * @see org.apache.shiro.mgt.SecurityManager
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm());
        manager.setCacheManager(cacheManager());
        manager.setSessionManager(defaultWebSessionManager());
        manager.setRememberMeManager(rememberMeManager());//记住我
        return manager;
    }

    /**
     * @return
     * @see DefaultWebSessionManager
     */
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(cacheManager());
        sessionManager.setGlobalSessionTimeout(Long.valueOf(ShiroProperties.getValueByKey("globalSessionTimeout")));
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);


/*
        sessionManager.setSessionValidationScheduler(sessionValidationScheduler());
*/
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionDAO(sessionDAO());
        return sessionManager;
    }
    @Bean
   public  EnterpriseCacheSessionDAO sessionDAO(){
       EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
       sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
       sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
       return  sessionDAO;
   }


    /**
     * @return
     * @see MyShiroRelam--->AuthorizingRealm
     */
    @Bean
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public MyShiroRelam userRealm() {
        MyShiroRelam userRealm = new MyShiroRelam();
        userRealm.setCacheManager(cacheManager());
        userRealm.setAuthorizationCachingEnabled(false);
        return userRealm;
    }


    @Bean
    public FilterRegistrationBean urlPermissionsFilterRegistrationBean(UrlPermissionsFilter urlPermissionsFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(urlPermissionsFilter);
        registration.setEnabled(false);//设置不自动在在配置时执行,默认是true
        return registration;
    }

    @Bean(name = "urlPermissionsFilter")
    public UrlPermissionsFilter urlPermissionsFilter() {
        return new UrlPermissionsFilter();
    }

    @Bean
    public FilterRegistrationBean sessionFilterRegistrationBean(SessionFilter sessionFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(sessionFilter);
        registration.setEnabled(false);//设置不自动在在配置时执行,默认是true
        return registration;
    }

    @Bean(name = "sessionFilter")
    public SessionFilter sessionFilter() {
        return new SessionFilter();
    }

    //    @Bean
//    public EhCacheManager cacheManager() {
//        EhCacheManager cacheManager = new EhCacheManager();
//        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
//        return cacheManager;
//    }
    @Bean
    public RedisCacheManager cacheManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setExpire(Integer.valueOf(RedisProperties.getValueByKey("spring.redis.timeout")));
        redisManager.setHost(RedisProperties.getValueByKey("spring.redis.hostName"));
        redisManager.setPort(Integer.valueOf(RedisProperties.getValueByKey("spring.redis.port")));
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager);
        return cacheManager;
    }

    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter() {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        kickoutSessionControlFilter.setCacheManager(cacheManager());
        kickoutSessionControlFilter.setSessionManager(defaultWebSessionManager());
        kickoutSessionControlFilter.setKickoutAfter(false);
        kickoutSessionControlFilter.setMaxSession(1);
        kickoutSessionControlFilter.setKickoutUrl("/kickout?kickout=1");
        kickoutSessionControlFilter.setEnabled(false);//设置不自动在在配置时执行,默认是true
        return kickoutSessionControlFilter;
    }




    ///////////////////////////////
    @Bean
    public QuartzSessionValidationScheduler sessionValidationScheduler() {
        QuartzSessionValidationScheduler scheduler = new QuartzSessionValidationScheduler();

        scheduler.setSessionManager(defaultWebSessionManager());

        scheduler.setSessionValidationInterval(Long.valueOf(ShiroProperties.getValueByKey("sessionValidationInterval")));
        return  scheduler;
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie(ShiroProperties.getValueByKey("sessionIdCookieName"));
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);//<!-- maxAge=-1表示浏览器关闭时失效此Cookie； -->
        return simpleCookie;
    }


    /**记住我
     * cookie对象;
     * @return
     */
    public SimpleCookie rememberMeCookie(){
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 , 2592000单位秒;-->
        simpleCookie.setMaxAge(Integer.valueOf(ShiroProperties.getValueByKey("rememberMeCookieMaxAge")));
        return simpleCookie;
    }
    /**
     * cookie管理对象;记住我功能
     * @return
     */
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor obj = new AuthorizationAttributeSourceAdvisor();
        obj.setSecurityManager(securityManager());
        return obj;
    }

}
