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
        levelUsers.add(new LevelUser("aa", "111", "level1"));
        levelUsers.add(new LevelUser("bb", "222", "level1"));
        levelUsers.add(new LevelUser("cc", "333", "level2"));
        levelUsers.add(new LevelUser("dd", "444", "level2"));
        levelUsers.add(new LevelUser("ee", "555", "level3"));
        levelUsers.add(new LevelUser("ff", "666", "level3"));
        levelUsers.add(new LevelUser("root", "tool", "admin,level1,level2,level3"));
    }

    public LevelUser getLevelUserByName(String name) {
        List<LevelUser> collect = levelUsers.stream().filter((obj) -> obj.getUsername().equals(name)).collect(Collectors.toList());
        if (collect != null && collect.size() > 0) {
            return collect.get(0);
        }
        return null;
    }

}
