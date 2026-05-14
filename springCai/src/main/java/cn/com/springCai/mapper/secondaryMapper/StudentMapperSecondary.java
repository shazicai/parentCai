package cn.com.springCai.mapper.secondaryMapper;

import cn.com.springCai.service.entity.student.po.Student;
import cn.com.springCai.service.entity.student.vo.StudentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author caiJH
 * @Date 2025/2/19 2:55 PM
 * @Version 1.0
 */
@Repository
public interface StudentMapperSecondary {
    /**
     * 新增
     * @param student
     * @return
     */
    int save(Student student);

    /**
     * 更新
     * @param student
     * @return
     */
    int update(Student student);

    /**
     * 查询
     * @return
     */
    List<StudentVo> list(@Param("name") String name);

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
