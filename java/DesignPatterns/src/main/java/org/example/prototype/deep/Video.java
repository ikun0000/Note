package org.example.prototype.deep;

import java.util.Date;

public class Video implements Cloneable {

    private String name;
    private String format;
    private Date createTime;

    public Video(String name, String format, Date createTime) {
        this.name = name;
        this.format = format;
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Video{" +
                "name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();

        Video clone1 = (Video) clone;
        clone1.setCreateTime((Date) clone1.createTime.clone());

        return clone;
    }
}
