package com.phoenix.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 手机认证处理提供者
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/17 14:45
 */
public class MobileAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 认证处理：
     * 1.通过手机号码，查询用户信息 （交给UseDetailService来实现）
     * 2.当查询到用用户对象，则认证通过，封装Authentication对象
     *
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        //获取手机号码
        String mobile = (String) mobileAuthenticationToken.getPrincipal();
        //1.通过手机号码，查询用户信息 （交给UseDetailService来实现）
        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);
        if (userDetails == null) {
            //根据手机号未查询到用户信息，抛出异常
            throw new AuthenticationServiceException("该手机号未注册");
        }
        //认证通过
        //封装到MobileAuthenticationToken中
        MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(mobileAuthenticationToken.getDetails());
        return authenticationToken;
    }

    /**
     * 通过这个方法，来选择对应的Provider，即选择MobileAuthenticationProvider
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
