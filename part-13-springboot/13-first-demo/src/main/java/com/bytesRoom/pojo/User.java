package com.bytesRoom.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@Slf4j
public class User {


    private int userId;
    private String username;
    private String password;
    private String salt;
    private String phone;
    private String avatar;
    private int deptId;

    private Date createTime;
    private Date updateTime;

}
