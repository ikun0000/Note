package com.example.dao;

import com.example.entity.LevelUser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LevelUserService {

    private static List<LevelUser> levelUsers = new ArrayList<>();

    static {
        levelUsers.add(new LevelUser("aa", "$2a$10$fwlBFn35PfCQedW0M28VCOKihnwPCn0qEF7A4fYa6xmbLYkwD4y82", "level1"));  // 111
        levelUsers.add(new LevelUser("bb", "$2a$10$nwLkByhwD6Mnk0OzAhRfh.99zlTl2w7uUjyX4M5l8zq2ZdA2drQ6C", "level1"));  // 222
        levelUsers.add(new LevelUser("cc", "$2a$10$LL2m/u9bRPv1TZ96ZmgcceKjF.7AF49YmtQLlZikQ.0ur6QS9R/ge", "level2"));  // 333
        levelUsers.add(new LevelUser("dd", "$2a$10$miuVzRQp6uytGy2eUHyQeucKG97eS1xh1X50k.Fp1j2Fh1Q1avanK", "level2"));  // 444
        levelUsers.add(new LevelUser("ee", "$2a$10$zZjutzdEkTkKUtRiE4zwEuQ.ou/SyYcX.Qzbn8AYLJjT3ixY8lPya", "level3"));  // 555
        levelUsers.add(new LevelUser("ff", "$2a$10$x2ZdMMXNJsBX4RfwIK/jQeiqrt5/Ne.ooGcx5hpym3prXFqmhY9TC", "level3"));  // 666
        levelUsers.add(new LevelUser("root", "$2a$10$awVD1aaE6fo9k8jXwS7Gluu9kwQhTw7NJnEZi6QKaXamB7DmpNQPS", "admin")); // toor
    }

    public LevelUser getLevelUserByName(String name) {
        List<LevelUser> collect = levelUsers.stream().filter((obj) -> obj.getUsername().equals(name)).collect(Collectors.toList());
        if (collect != null && collect.size() > 0) {
            return collect.get(0);
        }
        return null;
    }

}
