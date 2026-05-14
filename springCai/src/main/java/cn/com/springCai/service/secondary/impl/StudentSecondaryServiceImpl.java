package cn.com.springCai.service.secondary.impl;

import cn.com.springCai.mapper.secondaryMapper.StudentMapperSecondary;
import cn.com.springCai.service.entity.student.po.Student;
import cn.com.springCai.service.entity.student.vo.StudentVo;
import cn.com.springCai.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Author caiJH
 * @Date 2025/2/24 5:28 PM
 * @Version 1.0
 */
@Service("studentSecondaryService")
public class StudentSecondaryServiceImpl implements StudentService {

    @Autowired
    private StudentMapperSecondary studentMapperSecondary;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;

    @Autowired
    @Qualifier("secondaryDataSource")
    private DataSource secondaryDataSource;


    /**
     * 根据名称模糊查询数据
     * @param name
     * @return
     */
    @Override
    public List<StudentVo> getList(String name) {
        return studentMapperSecondary.list(name);
    }

    /**
     * 修改数据并同步到新数据库
     * @param student
    */
     @Override
    public Integer update(Student student) {
         return studentMapperSecondary.update(student);
     }

    /**
     * 新增
     * @param student
     * @return
     */
    @Override
    public int save(Student student) {
        return studentMapperSecondary.save(student);
    }

    /**
     * 根据id获取对象
     * @param id
     * @return
     */
    @Override
    public Student getById(Integer id) {
        return studentMapperSecondary.getById(id);
    }
}
