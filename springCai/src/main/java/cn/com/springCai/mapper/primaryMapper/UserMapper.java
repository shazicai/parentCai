package cn.com.springCai.mapper.primaryMapper;

import cn.com.springCai.service.entity.student.vo.StudentVo;
import com.service.springService.user.entity.po.User;
import com.service.springService.user.entity.vo.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author caiJH
 * @Date 2025/2/19 2:55 PM
 * @Version 1.0
 */
@Repository
public interface UserMapper {
    /**
     * 新增
     * @param user
     * @return
     */
    int save(User user);

    /**
     * 更新
     * @param user
     * @return
     */
    int update(User user);

    /**
     * 查询
     * @return
     */
    List<UserVo> list(@Param("name") String name);

    /**
     * @param id
     * @return
     */
    StudentVo getById(@Param("id") Integer id);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int del(@Param("id") Integer id);
}
