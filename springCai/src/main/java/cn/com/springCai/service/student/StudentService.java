package cn.com.springCai.service.student;

import cn.com.springCai.service.entity.student.po.Student;
import cn.com.springCai.service.entity.student.vo.StudentVo;
import cn.com.springCai.test.applicationTest.bean.father.BaseService;

import java.util.List;

/**
 * @Author caiJH
 * @Date 2025/2/19 3:59 PM
 * @Version 1.0
 */
public interface StudentService {

    /**
     * 根据名称模糊查询数据
     * @param name
     * @return
     */
    List<StudentVo> getList(String name);

    /**
     * 修改数据并同步到新数据库
     * @param student
     */
    Integer update(Student student);

    /**
     * 新增
     * @param student
     * @return
     */
    int save(Student student);

    /**
     * 根据id获取对象
     * @param id
     * @return
     */
    Student getById(Integer id);
}
