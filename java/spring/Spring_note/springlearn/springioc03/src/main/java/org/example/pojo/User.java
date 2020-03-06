package org.example.pojo;

public class User {

    private String name;

//    public User() {
//        System.out.println("User constructor");
//    }


    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }

    public void show() {
        System.out.println("name=" + name);
    }
}
