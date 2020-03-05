package com.example.dto;

public class User {
    private String usernm;
    private String passwd;

    public String getUsernm() {
        return usernm;
    }

    public void setUsernm(String usernm) {
        this.usernm = usernm;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "usernm='" + usernm + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
