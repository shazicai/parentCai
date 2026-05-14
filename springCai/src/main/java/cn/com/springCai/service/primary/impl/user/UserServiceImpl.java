package cn.com.springCai.service.primary.impl.user;

import cn.com.springCai.mapper.primaryMapper.UserMapper;
import cn.com.springCai.service.student.StudentService;
import cn.com.springCai.test.applicationTest.bean.father.BaseService;
import com.service.springService.user.entity.dto.UserDto;
import com.service.springService.user.entity.po.User;
import com.service.springService.user.service.UserService;
import com.service.springService.user.entity.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @Author caiJH
 * @Date 2025/4/16 9:51 AM
 * @Version 1.0
 */
@Service("userService")
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StudentService studentService;

//    @Autowired
//    private StudentServiceImpl studentService;

    // 测试单例对象在不同调用中是否值不会变更
    // 若无变更，需要注意多次请求拿到相同的数据
    private String name;

    /**
     * 查询用户集合
     * @param dto
     * @return
     */
    @Override
    public List<UserVo> getList(UserDto dto) {
        System.out.printf("name初始值为null:%s\n",name);
        setMmc("ASM");
        name = "蔡进辉";
        System.out.println("第一次调用父类数据mmc:" + getMmc());
        return Arrays.asList(new UserVo(1,"caiJinHui"),new UserVo(1,"caiSaiYan"));
    }

    /**
     * 查询单个用户信息
     * @param dto
     * @return
     */
    @Override
    public UserVo get(UserDto dto) {
        System.out.printf("非第一次调用获取name值:%s\n",name);
//        System.out.printf("非第一次调用studentService获取父类数据mmc:%s\n",studentService.getMmc());
        return new UserVo(1,"caiJinHui");
    }

    /**
     * 新增数据
     * @param
     */
    @Override
    @Transactional
    public void insert(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        userMapper.save(user);
        studentService.getList("cai");
    }
}
