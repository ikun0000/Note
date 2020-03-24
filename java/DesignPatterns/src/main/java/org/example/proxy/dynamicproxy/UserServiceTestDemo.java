package org.example.proxy.dynamicproxy;

import org.example.proxy.staticproxy.demo2.UserService;
import org.example.proxy.staticproxy.demo2.UserServiceImpl;

public class UserServiceTestDemo {

    public static void main(String[] args) {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        ProxyServiceInvocationHandler proxyServiceInvocationHandler = new ProxyServiceInvocationHandler();
        proxyServiceInvocationHandler.setTarget(userServiceImpl);
        UserService userService = (UserService) proxyServiceInvocationHandler.getProxy();

        userService.create();
        userService.query();
        userService.update();
        userService.delete();
    }

}
