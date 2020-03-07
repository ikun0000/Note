package com.jvm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
class Book {
    private Integer id;
    private String bookName;
    private Double price;
}

@Accessors(chain = true)
class User {
    private Integer id;
    private String userName;
    private Integer age;

    public User(Integer id, String userName, Integer age) {
        this.id = id;
        this.userName = userName;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

public class FunctionalInterfaceAndStreamDemo {

    public static void main(String[] args) {
        lombokChain();


    }

    public static void lombokChain() {
//        Book book1 = new Book();
//        book1.setId(1);
//        book1.setBookName("C++ Primer Plus");
//        book1.setPrice(99.99);
//
//        Book book2 = new Book();
//        book2.setId(2)
//                .setBookName("Thinking in JAVA")
//                .setPrice(55.55);

        List<User> userList = Arrays.asList(new User(11, "a", 23),
                new User(12, "b", 23),
                new User(13, "c", 22),
                new User(14, "d", 28),
                new User(16, "e", 26));

        // java.util.function.Function
        // java.util.function.Predicate
        // java.util.function.Supplier
        // java.util.function.consumer
        userList.stream()
                .filter(user -> user.getId() % 2 == 0)
                .filter(user -> user.getAge() > 24)
                .map(user -> user.getUserName().toUpperCase())
                .sorted((name1, name2) -> {return -name1.compareTo(name2);})
                .limit(1)
                .forEach(System.out::println);
    }

}
