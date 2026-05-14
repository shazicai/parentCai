package com.service.springService.user.service;

import com.service.springService.user.entity.dto.UserDto;
import com.service.springService.user.entity.po.User;
import com.service.springService.user.entity.vo.UserVo;

import java.util.List;

/**
 * @Author caiJH
 * @Date 2025/4/16 9:47 AM
 * @Version 1.0
 */
public interface UserService {

    /**
     * 查询用户集合
     * @param dto
     * @return
     */
    List<UserVo> getList(UserDto dto);

    /**
     * 查询单个用户信息
     * @param dto
     * @return
     */
    UserVo get(UserDto dto);

    /**
     * 新增数据
     * @param dto
     */
    void insert(UserDto dto);
}
