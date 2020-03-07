package com.example.springbootrestfulapicrud.component;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class MyLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String l = httpServletRequest.getParameter("l");        // 获取携带的语言信息
        Locale locale = null;           // 返回的语言信息

        if (!StringUtils.isEmpty(l)) {
            try {
                String[] split = l.split("_");          // 分割国家代码和语言信息
                locale = new Locale(split[0], split[1]);
            } catch (Exception ex) {
                locale = Locale.getDefault();
            }
        } else {
            locale = Locale.getDefault();               // 使用系统默认的区域信息
        }

        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
