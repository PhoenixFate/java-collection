package com.phoenix.workflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 对应UEL 表达式 ${deptService.findManagerByUserId(userId)}
 */
@Service
public class DeptService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeptService.class);

    /**
     * 通过用户id查询上级领导人作为办理人
     * @param userId 用户id
     * @return 上级领导
     */
    public String findManagerByUserId(String userId) {
        LOGGER.info("DeptService.findManagerByUserId 查询userId： {} 作为上级领导", userId);
        return "小梦";
    }

}
