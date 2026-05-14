package com.spc.springweb.base;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author caiJH
 * @Date 2025/4/15 5:12 PM
 * @Version 1.0
 */
public class BaseController {

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    protected HttpSession getCurrentSession(){
        return getRequest().getSession();
    }
}
