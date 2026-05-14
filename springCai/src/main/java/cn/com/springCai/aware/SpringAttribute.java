package cn.com.springCai.aware;

import cn.com.springCai.service.entity.user.po.User;
import org.springframework.stereotype.Component;

/**
 * @Author caiJH
 * @Date 2025/3/14 4:50 PM
 * @Version 1.0
 */
@Component("springAttribute")
public class SpringAttribute implements AttributeAware {

    private User user;

    @Override
    public void setAttribute(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SpringAttribute{" +
                "user=" + user +
                '}';
    }
}
