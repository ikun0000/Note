package com.example.springbootrestfulapicrud.component;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object s = request.getSession().getAttribute("loginuser");
        if (s == null) {
            request.setAttribute("msg", "还没有登录");
            request.getRequestDispatcher("/login.html").forward(request, response);
            return false;
        }
        return true;
    }
}
