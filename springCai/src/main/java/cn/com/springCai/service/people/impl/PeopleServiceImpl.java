package cn.com.springCai.service.people.impl;

import cn.com.springCai.service.entity.people.po.People;
import cn.com.springCai.service.people.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author caiJH
 * @Date 2025/3/17 10:25 AM
 * @Version 1.0
 */
@Service("peopleService")
public class PeopleServiceImpl implements PeopleService {

    @Autowired
    private People people;

    @Override
    public void printPeople() {
        System.out.println(people.toString());
    }
}
