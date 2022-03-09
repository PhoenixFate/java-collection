package com.bytesRoom.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

// @Data
@Table(name = "sys_user")
@Data
public class User {

    @KeySql(useGeneratedKeys = true)
    @Id
    private Integer userId;
    private String username;

    private String password;
    private String salt;
    private String phone;
    private String avatar;
    //private int deptId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    //代表不会写入数据库
    @Transient
    @JsonIgnore
    private String note;

}
