package com.phoenix.blog.oauth2.service;

import com.phoenix.blog.entity.SysMenu;
import com.phoenix.blog.entity.SysUser;
import com.phoenix.blog.feign.FeignSystemService;
import com.phoenix.blog.oauth2.security.JwtUser;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author phoenix
 * @Date 10/21/22 21:14
 * @Version 1.0
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final FeignSystemService feignSystemService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //判断用户名是否为空
        if (StringUtils.isEmpty(username)) {
            throw new BadCredentialsException("用户名不能为空");
        }
        //通过用户名查询数据库中的用户信息
        SysUser sysUser = feignSystemService.findUserByUsername(username);
        if (sysUser == null) {
            throw new BadCredentialsException("用户名或者密码错误");
        }
        //通过用户id查询所有拥有的权限信息
        List<SysMenu> sysMenuList = feignSystemService.findMenuListByUserId(sysUser.getId());
        //封装权限信息（权限标识符code）
        List<GrantedAuthority> authorityList = null;
        if (CollectionUtils.isNotEmpty(sysMenuList)) {
            authorityList = new ArrayList<>();
            for (SysMenu sysMenu : sysMenuList) {
                //获得权限标识
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(sysMenu.getCode());
                authorityList.add(simpleGrantedAuthority);
            }
        }
        //构建UserDetails接口的实现类JwtUser对象
        return new JwtUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(),
                sysUser.getNickName(), sysUser.getImageUrl(), sysUser.getMobile(),
                sysUser.getEmail(), sysUser.getIsAccountNonExpired(), sysUser.getIsAccountNonLocked(),
                sysUser.getIsCredentialsNonExpired(), sysUser.getIsEnabled(), authorityList);
    }
}
