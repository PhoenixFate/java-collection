package com.phoenix.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.web.entity.SysPermission;
import com.phoenix.web.mapper.SysPermissionMapper;
import com.phoenix.web.service.SysPermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author phoenix
 * @Date 9/19/22 23:46
 * @Version 1.0
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Override
    public List<SysPermission> findPermissionByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return baseMapper.selectPermissionByUserId(userId);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        // 1.删除当前id
        // baseMapper.deleteById(id);
        // 2.删除parent_id=id
        // delete from sys_permission where  parent_id = #{id}
        // baseMapper.delete(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getParentId, id));

        if (id == null) {
            return false;
        }
        //要删除的所有评论id（通过递归获得）
        List<Long> idListToDelete = new ArrayList<>();
        idListToDelete.add(id);
        //递归所有的评论id，并将id装到要删除的集合中
        this.getAllIdListByParentId(idListToDelete, id);
        //批量删除集合中的评论id
        baseMapper.deleteBatchIds(idListToDelete);
        return true;
    }

    private void getAllIdListByParentId(List<Long> idListToDelete, Long parentId) {
        //查询子评论信息
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        List<SysPermission> permissionList = baseMapper.selectList(queryWrapper);
        //如果子评论不为空，则取出每条评论的评论id
        if (CollectionUtils.isNotEmpty(permissionList)) {
            for (SysPermission permission : permissionList) {
                //将当前查询到的评论id放到要删除的id集合中
                idListToDelete.add(permission.getId());
                //递归继续查询子评论id
                this.getAllIdListByParentId(idListToDelete, permission.getId());
            }
        }
    }
}
