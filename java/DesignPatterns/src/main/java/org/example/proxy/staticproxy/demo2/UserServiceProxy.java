package org.example.proxy.staticproxy.demo2;

public class UserServiceProxy implements UserService {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void create() {
        log("create");
        userService.create();
    }

    @Override
    public void query() {
        log("query");
        userService.query();
    }

    @Override
    public void update() {
        log("update");
        userService.update();
    }

    @Override
    public void delete() {
        log("delete");
        userService.delete();
    }

    private void log(String msg) {
        System.out.println("execute " + msg + " operation");
    }
}
