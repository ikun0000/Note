# Jackson返回不同的JSON

> 实现这种效果：
>
> 定义一个用户类，包括姓名，密码，年龄
>
> 当用户访问查询所有用户的接口`/user`时，返回一个用户列表，但是这个是公共接口，不能返回密码字段
>
> 当用户访问查询单个用户的接口`/user/{id}`时，返回i一个用户对象，这个是私有接口，假设只有通过认证的用户可以访问（自己访问自己），返回全部字段

定义实体类

```java
public class User {

    public interface AllUserView {};	// 定义接口，用于标识返回使用那张视图
    public interface AUserView {};

    private String username;
    private String password;
    private Integer age;

    public User(String username, String password, Integer age) {
        this.username = username;
        this.password = password;
        this.age = age;
    }

    @JsonView({AllUserView.class, AUserView.class})	// 表示访问AllUserView和AUserView都会返回这个属性
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView({AUserView.class})  // 表示只有访问AUserView才会返回这个属性
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView({AllUserView.class, AUserView.class})  // 表示访问AllUserView和AUserView都会返回这个属性
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
```

编写测试controller

```java
@RestController
public class HelloController {

    @GetMapping("/user")
    @JsonView({User.AllUserView.class})
    public List<User> users() {
        List<User> users = new ArrayList<>();
        users.add(new User("aa", "111", 22));
        users.add(new User("bb", "222", 20));
        return users;
    }

    @GetMapping("/user/{id}")
    @JsonView({User.AUserView.class})
    public User user() {
        return new User("cc", "333", 18);
    }

}
```



测试结果：

```shell
$ curl http://127.0.0.1:8080/user
[{"username":"aa","age":22},{"username":"bb","age":20}]

$ curl http://127.0.0.1:8080/user/1
{"username":"cc","password":"333","age":18}
```

