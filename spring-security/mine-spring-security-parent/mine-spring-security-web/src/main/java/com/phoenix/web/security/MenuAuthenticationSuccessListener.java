package com.phoenix.web.security;

import com.phoenix.core.authentication.AuthenticationSuccessListener;
import com.phoenix.web.entity.SysPermission;
import com.phoenix.web.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author phoenix
 * @Date 2022/9/27 10:47
 * @Version 1.0.0
 */
@Slf4j
@Component
public class MenuAuthenticationSuccessListener implements AuthenticationSuccessListener {

    /**
     * 查询用户所拥有的权限菜单
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 当用户认证成功后，会将认证对象传入
     */
    @Override
    public void successListener(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("------------ 查询用户拥有的权限菜单 ------------");
        Object principal = authentication.getPrincipal();
        if (principal instanceof SysUser) {
            SysUser sysUser = (SysUser) principal;
            //获取到了当前登录用户的菜单和按钮
            //过滤菜单（去掉按钮）
            loadMenuTree(sysUser);
        }
    }

    /**
     * 值加载菜单，不需要按钮
     *
     * @param sysUser 用户
     */
    private void loadMenuTree(SysUser sysUser) {
        //获取到了当前登录用户的菜单和按钮
        List<SysPermission> sysPermissionList = sysUser.getPermissions();
        if (CollectionUtils.isEmpty(sysPermissionList)) {
            return;
        }
        List<SysPermission> menuList = Lists.newArrayList();
        for (SysPermission sysPermission : sysPermissionList) {
            if (sysPermission.getType() == 1) {
                menuList.add(sysPermission);
            }
        }
        //2.获取子菜单
        for (SysPermission menu : menuList) {
            //存放当前菜单的所有子菜单
            List<SysPermission> childMenu = Lists.newArrayList();
            List<String> childUrl = new ArrayList<>();
            //获取menu菜单下的所有子菜单
            for (SysPermission p : menuList) {
                //如果p.parentId等于menu.id则就是它的子菜单
                if (p.getParentId().equals(menu.getId())) {
                    childMenu.add(p);
                    childUrl.add(p.getUrl());
                }
            }
            //设置子菜单
            menu.setChildren(childMenu);
            menu.setChildrenUrl(childUrl);
        }

        List<SysPermission> result = new ArrayList<>();
        for (SysPermission menu : menuList) {
            //如果父id是0，则是根菜单
            if (menu.getParentId().equals(0L)) {
                result.add(menu);
            }
        }
        //最终把获取到的根菜单和子菜单集合重新设置到permission集合中
        sysUser.setPermissions(result);
    }

}
