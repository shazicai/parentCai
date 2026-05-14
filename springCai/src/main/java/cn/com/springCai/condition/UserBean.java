package cn.com.springCai.condition;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * @Author caiJH
 * @Date 2025/4/28 3:50 PM
 * @Version 1.0
 * 当userBean满足UserConditional条件才能成为一个bean
 */
@Component
@Conditional(UserConditional.class)
public class UserBean {

    private String name;

}
