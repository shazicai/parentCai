package cn.com.springCai.service.primary.impl;

import cn.com.springCai.aware.SpringAttribute;
import cn.com.springCai.mapper.primaryMapper.StudentMapper;
import cn.com.springCai.myAnnotation.GetValue;
import cn.com.springCai.service.entity.student.po.Student;
import cn.com.springCai.service.entity.student.vo.StudentVo;
import cn.com.springCai.service.student.StudentService;
import cn.com.springCai.test.applicationTest.bean.father.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;


@Service("studentService")
public class StudentServiceImpl extends BaseService implements StudentService {

    @GetValue("${my.getValue.test}")
    private String name;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;

    @Autowired
    @Qualifier("secondaryDataSource")
    private DataSource secondaryDataSource;

    @Autowired
    private SpringAttribute springAttribute;

    /**
     * 根据名称模糊查询数据
     * @param name
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<StudentVo> getList(String name) {
        return studentMapper.list(name);
    }

    /**
     * 修改数据并同步到新数据库
     * @param id
     */
    @Override
    public Integer update(Student student) {
        System.out.printf("-----执行方法完毕-----%s\n",name);
        return studentMapper.update(student);
    }

    /**
     * 新增
     * @param student
     * @return
     */
    @Override
    public int save(Student student) {
        return studentMapper.save(student);
    }

    /**
     * 根据id获取对象
     * @param id
     * @return
     */
    @Override
    public Student getById(Integer id) {
        return studentMapper.getById(id);
    }
}
