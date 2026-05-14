package cn.com.springCai.aware;

import cn.com.springCai.service.entity.user.po.User;
import org.springframework.beans.factory.Aware;

/**
 * @Author caiJH
 * @Date 2025/3/14 4:49 PM
 * @Version 1.0
 */
public interface AttributeAware extends Aware {

    public void setAttribute(User user);
}
