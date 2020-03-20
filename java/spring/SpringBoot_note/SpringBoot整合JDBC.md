# SpringBoot整合JDBC

#### 导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.19</version>
</dependency>
```



#### 编写实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    private int id;
    private String name;
    private int age;
}
```



#### 操作数据库

SpringBoot整合JDBC后使用JdbcTemplete操作数据库

首先注入JdbcTempleate

```java
@Autowired
private JdbcTemplate jdbcTemplate;
```



查询所有并返回List

```java
jdbcTemplate.query("SELECT * FROM Student",
                new BeanPropertyRowMapper<Student>(Student.class));

// 定义
// public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException
```

BeanPropertyRowMapper会根据列名和属性名自动装配



查询单个

```java
jdbcTemplate.queryForObject("SELECT * FROM Student WHERE id = ?",
                (rs, rowNum) -> {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setAge(rs.getInt("age"));
                    return student;
                },
                id);

// 定义
// public <T> T queryForObject(String sql, RowMapper<T> rowMapper, @Nullable Object... args) throws DataAccessException
```

实现RowMapper自定义装配规则，RowMapper是函数是接口



添加，修改，删除调用的方法都是一样的

添加一条记录

```java
jdbcTemplate.update("INSERT INTO Student (name, age) VALUES (?, ?)",
                student.getName(), student.getAge());

jdbcTemplate.update("DELETE FROM Student WHERE id = ?", id);

jdbcTemplate.update("UPDATE Student SET name = ?, age = ? WHERE id = ?",
                student.getName(), student.getAge(), student.getId());

// 定义
// public int update(String sql, @Nullable Object... args) throws DataAccessException 
```

