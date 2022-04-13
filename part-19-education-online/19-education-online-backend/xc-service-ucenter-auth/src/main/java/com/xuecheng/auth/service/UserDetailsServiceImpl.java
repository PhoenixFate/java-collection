package com.xuecheng.auth.service;

import com.xuecheng.auth.client.UserCenterClient;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * spring security Oauth2 的配置，重写loadUserByUsername
 * 用户调用Oauth2的密码模式的时候，会调用loadUserByUsername方法，
 * 先会认证请求头中的basic认证中的username XcWebApp和 password XcWebApp
 * 然后如果返回UserDetails为null，则失败，返回带数据的UserDetails，会
 * 校验UserDetails中的password
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserCenterClient userCenterClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpBasic认证，httpBasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if (authentication == null) {
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if (clientDetails != null) {
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username, clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        //远程调用查询用户
        XcUserExt userExtension = userCenterClient.getUserExtensionByUsername(username);
        if (userExtension == null) {
            //返回空，账号不存在，则认证失败
            return null;
        }
        //取出正确密码（hash值）
        String password = userExtension.getPassword();
        List<String> user_permission = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userExtension.getPermissions())) {
            userExtension.getPermissions().forEach(item -> user_permission.add(item.getCode()));
        }

        UserJwt userDetails = new UserJwt(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.join(user_permission.toArray(), ",")));
        userDetails.setId(userExtension.getId());
        userDetails.setUtype(userExtension.getUtype());//用户类型
        userDetails.setCompanyId(userExtension.getCompanyId());//所属企业
        userDetails.setName(userExtension.getName());//用户名称
        userDetails.setUserpic(userExtension.getUserpic());//用户头像
       /* UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(""));*/
//                AuthorityUtils.createAuthorityList("course_get_baseinfo","course_get_list"));
        return userDetails;
    }

}
