package cn.com.springCai.service.attribute.impl;

import cn.com.springCai.aware.SpringAttribute;
import cn.com.springCai.service.attribute.SpringAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author caiJH
 * @Date 2025/3/17 10:33 AM
 * @Version 1.0
 */
@Service("springAttributeService")
public class SpringAttributeServiceImpl implements SpringAttributeService {

    @Autowired
    private SpringAttribute springAttribute;

    @Override
    public void printAttribute() {
        System.out.println(springAttribute.toString());
    }
}
