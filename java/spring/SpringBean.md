# SpringBean配置



### XML配置Bean

使用maven创建项目，一般新建的话在src/main下是没有resource目录的，自己建一个，里面的xml文件用来配置Bean

Bean的xml基本配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
        http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.1.xsd">

    <!-- 这里写自己的Bean -->

</beans>
```





#### 普通类的Bean配置

在src下面建好包然后创建一个类

```java
package org.czk;

public class People {
    protected String name;
    protected int age;


    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void info() {
        System.out.println("Name: " + this.name);
        System.out.println("Age: " + this.age);
    }
}

```

然后在xml文件中配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xmlns:p="http://www.springframework.org/schema/p"
               xmlns:util="http://www.springframework.org/schema/util"
               xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
        http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.1.xsd">

    <beans>
        <bean id="people" class="org.czk.People">
            <constructor-arg name="name" type="String" value="aaa"></constructor-arg>
            <constructor-arg name="age" type="int" value="1"></constructor-arg>

            <property name="name" value="aaa"></property>
            <property name="age" value="1"></property>
        </bean>
    </beans>

</beans>
```

这样就可以把People类交给IoC容器管理



```xml
<constructor-arg name="name" type="String" value="aaa"></constructor-arg>
<constructor-arg name="age" type="int" value="1"></constructor-arg>
```

这个标签用来使用构造器初始化类，name是参数名，type要指定类型，如果是简单数据类型用value初始化值，复杂类型或者用其他bean初始化用ref



```xml
<property name="name" value="aaa"></property>
<property name="age" value="1"></property>
```

这个标签用来使用set方法初始化bean，name和value和构造器的一样



然后如果要使id值来获取Bean

```java
package org.czk;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("springbean.xml");	// 获取应用程序上下文，使用xml配置的话要实例出一个ClassPathXmlApplicationContext对象

        People p = context.getBean("people", People.class);		// 这样就可以向IoC容器获得一个实例了

        p.info();
    }
}

```







#### 继承关系的Bean的配置

在上面的基础上加多一个类继承People类

```java
package org.czk;

public class Student extends People {
    private String id;


    public Student(String name, int age, String id) {
        super(name, age);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void info() {
        System.out.println("Student ID: " + this.id);
        super.info();
    }
}

```



xml的配置改成如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xmlns:p="http://www.springframework.org/schema/p"
               xmlns:util="http://www.springframework.org/schema/util"
               xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
        http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.1.xsd">

    <beans>
        <bean id="people" class="org.czk.People">
            <constructor-arg name="name" type="String" value="aaa"></constructor-arg>
            <constructor-arg name="age" type="int" value="1"></constructor-arg>

            <property name="name" value="aaa"></property>
            <property name="age" value="1"></property>
        </bean>

        <bean id="student" class="org.czk.Student" parent="people">
            <constructor-arg name="id" type="String" value="111111111"></constructor-arg>

            <property name="id" value="111111111"></property>
        </bean>

    </beans>

</beans>
```



要改的就是将bean标签加上parent，并把它指向父类bean的id

```xml
<bean id="student" class="org.czk.Student" parent="people">
    <constructor-arg name="id" type="String" value="111111111"></constructor-arg>

    <property name="id" value="111111111"></property>
</bean>
```





#### 工厂模式初始化Bean

创建一个工厂类

```java
package org.czk;

import java.util.List;

public class HumanFactory {

    public static People getPeople() {
        return new People("aaa", 100);
    }

    public static Student getStudent() {
        return new Student("bbb", 22, "22222222");
    }

}
```

在xml中加上

```xml
<bean id="peoplepeople" class="org.czk.HumanFactory" factory-method="getPeople"></bean>
<bean id="studentstudent" class="org.czk.HumanFactory" factory-method="getStudent"></bean>
```







#### 复杂数据类型的Bean配置

定义一个类来包含Student类的列表

```java
package org.czk;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private List<Student> allStudent;
    private String className;

    public Class(List<Student> allStudent, String className) {
        this.allStudent = allStudent;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void addStudent(Student student) {
        allStudent.add(student);
    }

    public void removeStudent(Student student) {
        allStudent.remove(student);
    }

    public List<Student> getAllStudent() {
        return allStudent;
    }

    @Override
    public String toString() {
        return "Class{" +
                "allStudent=" + allStudent +
                ", className='" + className + '\'' +
                '}';
    }
}

```



在上面xml文件中的beans加上

```java
<bean id="claxx" class="org.czk.Class">
    <constructor-arg name="className" type="String" value="class 1"></constructor-arg>
        <constructor-arg name="allStudent" ref="student"></constructor-arg>

        <property name="className" value="class 1"></property>
</bean>
```





#### Bean别名

这样可以用student2来获得Student的实例，name指向要取别名的Bean的id

```xml
<alias name="student" alias="student2"></alias>
```

