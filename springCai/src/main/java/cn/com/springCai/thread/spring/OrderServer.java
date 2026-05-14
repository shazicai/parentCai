package cn.com.springCai.thread.spring;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author caiJH
 * @Date 2026/4/16 11:08 AM
 * @Version 1.0
 */
@Service("orderServer")
public class OrderServer {

    @Async
    public void sendEmail(String to, String content){
        System.out.println("邮件已发送" + to);
    }
}
