package org.example.proxy.staticproxy.demo2;

public class TestDemo {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        UserServiceProxy userServiceProxy = new UserServiceProxy();
        userServiceProxy.setUserService(userService);

        userServiceProxy.create();
        userServiceProxy.query();
        userServiceProxy.update();
        userServiceProxy.delete();

    }

}
