package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("用户是否可用")
    private Boolean enable;
    @ApiModelProperty("用户是否过期")
    private Boolean accountExpire;
    @ApiModelProperty("用户是否被锁定")
    private Boolean accountLocked;
    @ApiModelProperty("用户角色")
    private String role;
}
