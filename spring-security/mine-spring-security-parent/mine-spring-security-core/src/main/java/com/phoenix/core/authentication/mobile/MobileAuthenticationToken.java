package com.phoenix.core.authentication.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * 模仿UsernamePasswordAuthenticationToken，新建用于mobile的token类
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/17 10:48
 */
public class MobileAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    // ~ Instance fields
    // ================================================================================================

    /**
     * 认证前是手机号，认证后是用户信息
     */
    private final Object principal;

    // ~ Constructors
    // ===================================================================================================

    /**
     * 认证之前使用的构造方法，此方法会标识未认证
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     */
    public MobileAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal; //认证前存放手机号
        setAuthenticated(false);
    }

    /**
     * 认证通过后，会重新创建MobileAuthenticationToken实例，来进行封装认证信息
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param principal   用户信息
     * @param authorities 权限资源集合
     */
    public MobileAuthenticationToken(Object principal,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;//认证前存放手机号，认证后存放用户信息
        super.setAuthenticated(true); // must use super, as we override
    }

    // ~ Methods
    // ========================================================================================================

    /**
     * 因为是父类中的抽象方法，所以要实现，但返回空
     * @return null
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}

