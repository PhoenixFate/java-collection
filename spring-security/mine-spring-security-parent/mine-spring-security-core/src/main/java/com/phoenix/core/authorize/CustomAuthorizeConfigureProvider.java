package com.phoenix.core.authorize;

import com.phoenix.core.property.SpringSecurityProperties;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 身份认证相关的授权配置
 */
@Component
@AllArgsConstructor
@Order(Integer.MAX_VALUE) //最后加载：值越小越优先，值越大加载越靠后
public class CustomAuthorizeConfigureProvider implements AuthorizeConfigureProvider {

    private SpringSecurityProperties springSecurityProperties;

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(springSecurityProperties.getAuthentication().getLoginPage(),
                springSecurityProperties.getAuthentication().getImageCodeUrl(),
                springSecurityProperties.getAuthentication().getMobileCodeUrl(),
                springSecurityProperties.getAuthentication().getMobilePage()
        ).permitAll();  //放行 不需要认证访问
        //这个应该在最后：其他请求都要通过身份认证
        config.anyRequest().authenticated(); //所有访问该应用的http请求都需要通过身份认证才可以访问
    }
}
