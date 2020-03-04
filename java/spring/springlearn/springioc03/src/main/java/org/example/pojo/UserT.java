package org.example.pojo;

public class UserT {
    private String name;

    public UserT() {
        System.out.println("userT constructor");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserT{" +
                "name='" + name + '\'' +
                '}';
    }
}
