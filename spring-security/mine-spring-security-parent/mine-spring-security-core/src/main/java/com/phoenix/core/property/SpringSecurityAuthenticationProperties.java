package com.phoenix.core.property;

import lombok.Data;

@Data
public class SpringSecurityAuthenticationProperties {

    private String loginPage;

    private String loginProcessingUrl;

    private String usernameParameter;

    private String passwordParameter;

    private String[] staticPath;

}
