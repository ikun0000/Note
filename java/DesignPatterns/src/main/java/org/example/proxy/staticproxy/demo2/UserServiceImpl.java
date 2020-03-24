package org.example.proxy.staticproxy.demo2;

public class UserServiceImpl implements UserService {
    @Override
    public void create() {
        System.out.println("create user");
    }

    @Override
    public void query() {
        System.out.println("query user");
    }

    @Override
    public void update() {
        System.out.println("update user");
    }

    @Override
    public void delete() {
        System.out.println("delete user");
    }
}
