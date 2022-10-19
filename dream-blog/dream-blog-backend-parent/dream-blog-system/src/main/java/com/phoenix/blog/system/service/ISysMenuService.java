package com.phoenix.blog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.SysMenu;
import com.phoenix.blog.system.request.SysMenuRequest;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author phoenix
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 条件查询菜单列表
     *
     * @param req 请求参数
     * @return 菜单列表（树状结构）
     */
    Result queryList(SysMenuRequest req);

    /**
     * 根据菜单id删除
     *
     * @param id 菜单id
     * @return 是否删除成功
     */
    Result deleteById(String id);

    /**
     * 通过用户id查询所拥有的权限菜单树
     *
     * @param userId 用户id
     * @return 某用户的所有权限菜单数
     */
    Result findUserMenuTree(String userId);
}
