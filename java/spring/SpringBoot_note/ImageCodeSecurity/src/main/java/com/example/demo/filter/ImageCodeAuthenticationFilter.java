package com.example.demo.filter;

import com.example.demo.controller.ImageCodeController;
import com.example.demo.entity.ImageCode;
import com.example.demo.exception.ImageCodeException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ImageCodeAuthenticationFilter extends OncePerRequestFilter {

    public void vertify(HttpServletRequest request) throws ImageCodeException {
        ImageCode imageCode = (ImageCode) request.getSession().getAttribute(ImageCodeController.SESSION_KEY);
        String code = request.getParameter("imagecode");
        request.getSession().removeAttribute(ImageCodeController.SESSION_KEY);

        if (StringUtils.isEmpty(code)) {
            throw new ImageCodeException("验证码不能为空");
        }

        if (imageCode == null) {
            throw new ImageCodeException("验证码不存在");
        }

        if (imageCode.isExpried()) {
            throw new ImageCodeException("验证码以过期");
        }

        if (!StringUtils.equals(imageCode.getCode(), code)) {
            throw new ImageCodeException("验证码错误");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/loginproc", request.getRequestURI()) &&
                StringUtils.equalsIgnoreCase(request.getMethod(),"post")) {
            try {
                vertify(request);
            } catch (ImageCodeException exception) {
                response.sendRedirect("/loginpage");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
