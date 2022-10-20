package com.phoenix.blog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.SysUser;
import com.phoenix.blog.system.request.RegisterRequest;
import com.phoenix.blog.system.request.SysUserCheckPasswordRequest;
import com.phoenix.blog.system.request.SysUserRequest;
import com.phoenix.blog.system.request.SysUserUpdatePasswordRequest;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author phoenix
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 条件分页查询用户列表
     *
     * @param req
     * @return
     */
    Result queryPage(SysUserRequest req);

    /**
     * 根据用户id查询当前用户所拥有的角色ids
     *
     * @param id 用户id
     * @return
     */
    Result findRoleIdsById(String id);

    /**
     * 新增用户角色关系表数据
     *
     * @param userId  用户id
     * @param roleIds 角色id集合
     * @return
     */
    Result saveUserRole(String userId, List<String> roleIds);

    /**
     * 根据用户id进行删除，假删除，将is_enabled 状态值更新为0
     *
     * @param id 用户id
     * @return
     */
    Result deleteById(String id);

    /**
     * 校验原密码是否正确
     *
     * @param req
     * @return
     */
    Result checkPassword(SysUserCheckPasswordRequest req);

    /**
     * 修改用户密码
     *
     * @param req
     * @return
     */
    Result updatePassword(SysUserUpdatePasswordRequest req);

    /**
     * 更新用户信息
     *
     * @param sysUser
     * @return
     */
    Result update(SysUser sysUser);

    /**
     * 查询总用户数
     *
     * @return
     */
    Result getUserTotal();

    /**
     * 校验用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    Result checkUsername(String username);

    /**
     * 注册用户
     *
     * @param req
     * @return
     */
    Result register(RegisterRequest req);

}
