package cn.com.springCai;

import cn.com.springCai.service.attribute.SpringAttributeService;
import cn.com.springCai.service.entity.student.po.Student;
import cn.com.springCai.service.entity.student.vo.StudentVo;
import cn.com.springCai.service.people.PeopleService;
import cn.com.springCai.service.primary.impl.StudentServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@ImportResource("classpath:dubbo-provider.xml")
public class SpringCaiApplication {

    public static void main(String[] args) {
        System.out.println( "Hello World!" );
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder().sources(SpringCaiApplication.class).web(false).run(args);
        try {
            System.out.println("\n\n*********************springCai服务发布成功！*********************\n\n");
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            System.out.println( "启动异常" );
        }
        ctx.close();
    }

    public void test(){
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringCaiApplication.class);
        SpringAttributeService springAttributeService = (SpringAttributeService)ctx.getBean("springAttributeService");
        springAttributeService.printAttribute();
        PeopleService peopleService = (PeopleService)ctx.getBean("peopleService");
        peopleService.printPeople();
        StudentServiceImpl studentService = (StudentServiceImpl)ctx.getBean("studentService");
        List<StudentVo> list = studentService.getList("cjh");
        for (StudentVo vo : list) {
            System.out.println(vo.toString());
            Student student = new Student();
            student.setName(vo.getName());
            student.setClazz(vo.getClazz());
            student.setAddress(vo.getAddress());
            studentService.update(vo);
            System.out.println("---返回结果:---" + studentService.update(vo));
        }
    }

}
