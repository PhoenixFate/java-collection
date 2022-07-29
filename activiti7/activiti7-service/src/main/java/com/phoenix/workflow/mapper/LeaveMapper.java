package com.phoenix.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phoenix.workflow.entity.Leave;
import com.phoenix.workflow.request.LeaveRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LeaveMapper extends BaseMapper<Leave> {

    /**
     * 分页条件查询请假申请列表数据
     *
     * @param page 分页对象，mybatis-plus 规定分页对象一定要作为第一个参数
     * @return 请假列表
     */
    IPage<Leave> getLeaveList(IPage page, @Param("request") LeaveRequest leaveRequest);

}
