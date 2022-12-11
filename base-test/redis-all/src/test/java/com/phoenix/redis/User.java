package com.phoenix.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author phoenix
 * @Date 12/10/22 19:31
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class User {

    private Integer id;

    private String nickname;

    private Integer age;

    private String address;

}
