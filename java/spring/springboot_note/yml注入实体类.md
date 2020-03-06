### SpringBoot使用yml注入实体类

Entity:

```java
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dog {
    private String name;
    private int age;
}

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ConfigurationProperties("person")
public class Person {
    private String name;
    private Integer age;
    private Boolean happy;
    private Date birthday;
    private Map<String, String> maps;
    private List<String> list;
    private Dog dog;
    @Value("${pp.blog}")
    private String blog;
}
```

properties:

```yaml
person:
  name: aa
  age: 20
  happy: false
  birthday: 5/23/2001
  maps: {game: gta, eat: burger}
  list: [aa, bb, cc]
  dog:
    name: bb
    age: 2

pp.blog: http://example.com
```