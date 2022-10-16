//package com.phoenix.oauth2.resource.config;
//
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//
///**
// * 这个不需要了，已经移动到gateway之中了
// * @Author phoenix
// * @Date 10/5/22 23:48
// * @Version 1.0
// */
//@Configuration
//@EnableResourceServer //资源服务器的注解: 标识为资源服务(资源服务有校验令牌的功能)，开启之后就需要带着token来访问资源
//@EnableGlobalMethodSecurity(prePostEnabled = true) //开启方法级别权限控制 默认为false
//@AllArgsConstructor
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//   private final TokenStore tokenStore;
//
//   public static final String RESOURCE_ID = "product-server";
//
//   @Override
//   public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//       //当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源的id的权限
//       resources.resourceId(RESOURCE_ID)
//               // .tokenServices(tokenServices()) //使用远程检验token，使用JWT之后不再需要使用远程校验token
//               .tokenStore(tokenStore) //使用JWT令牌
//       ;
//   }
//
//   /**
//    * 使用JWT之后就不需要远程检查token了，直接使用JWT校验token
//    *
//    * @return ResourceServerTokenServices
//    */
//   public ResourceServerTokenServices tokenServices() {
//       //远程认证服务器进行校验 token是否有效
//       RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
//       //请求认证服务器校验地址，默认情况下，这个地址在认证服务器是拒绝访问的，需修改认证服务器中的校验地址为通过验证可访问
//       remoteTokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:8643/auth/oauth/check_token");
//       remoteTokenServices.setClientId("spring-security-pc");
//       remoteTokenServices.setClientSecret("pc-secret");
//       return remoteTokenServices;
//   }
//
//   @Override
//   public void configure(HttpSecurity http) throws Exception {
//       //关闭session机制，只是使用token访问
//       //SpringSecurity不会使用也不会创建HttpSession实例
//       http.sessionManagement()
//               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//               .and()
//               //配置某些资源对应的scope范围
//               .authorizeRequests()
//               //先匹配原则，匹配上则放行
//               .antMatchers("/product/list").hasAuthority("sys:user:list") //可以用注解，只是展示一下可以这么写
//               //所有请求都需要 针对客户端配置的all（scope）范围
//               .antMatchers("/**").access("#oauth2.hasScope('PRODUCT_API')")
//       ;
//
//   }
//}