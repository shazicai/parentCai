package com.spc.springweb.controller;

import com.service.springService.user.entity.dto.UserDto;
import com.service.springService.user.entity.req.UserReq;
import com.service.springService.user.service.UserService;
import com.service.springService.user.entity.vo.UserVo;
import com.spc.springweb.base.APIResponse;
import com.spc.springweb.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author caiJH
 * @Date 2025/4/15 5:06 PM
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

//    @Resource
    @Autowired
//    @Qualifier("userServiceImpl")
    private UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public APIResponse list(@RequestBody UserReq userReq){
        List<UserVo> list = userService.getList(new UserDto(userReq.getId(),userReq.getName()));
        return new APIResponse(list);
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public APIResponse get(@RequestBody UserReq userReq){
        UserVo vo = userService.get(new UserDto(userReq.getId(),userReq.getName()));
        return new APIResponse(vo);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public APIResponse insert(@RequestBody UserReq userReq){
        userService.insert(new UserDto(null,userReq.getName()));
        return new APIResponse("新增成功");
    }

    private void test(){
        // 某对象是否是此class文件的对象
        boolean boolClass = UserController.class.isAssignableFrom(UserController.class);

        // 对象是否是某个类
        UserController userController = new UserController();
        boolean boolObject = userController instanceof UserController;
    }
}
