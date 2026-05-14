package com.service.springService.user.entity.dto;

import java.io.Serializable;


public class UserDto implements Serializable {

    public UserDto() {
    }

    public UserDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
