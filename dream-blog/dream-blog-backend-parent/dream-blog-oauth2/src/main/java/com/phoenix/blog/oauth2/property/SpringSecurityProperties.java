package com.phoenix.blog.oauth2.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component //添加到容器中
@Data
@ConfigurationProperties(prefix = "mine.security")
public class SpringSecurityProperties {

    /**
     * 会将mine.security.authentication 下面的值绑定到SpringSecurityAuthenticationProperties对应的属性
     */
    private SpringSecurityAuthenticationProperties authentication;

}
