package org.example.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * dept entity
 * @author ikun
 * @date 2020/03/13
 */
@Data
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Dept implements Serializable {
    private long deptId;
    private String deptName;
    private String dbSource;

    public Dept(String deptName) {
        this.deptName = deptName;
    }
}
