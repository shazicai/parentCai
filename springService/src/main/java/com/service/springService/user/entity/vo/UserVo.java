package com.service.springService.user.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author caiJH
 * @Date 2025/4/15 5:32 PM
 * @Version 1.0
 */
@Data
public class UserVo implements Serializable {
    private Integer id;

    private String name;

    public UserVo() {

    }

    public UserVo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
