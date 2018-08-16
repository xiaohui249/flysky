package com.sean.flysky.mysql.model;

import com.sean.flysky.mysql.dbutil.BaseModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 测试实体类
 *
 * @author xiaoh
 * @create 2018-08-11 10:57
 **/
public class Persons extends BaseModel implements Serializable {

    private String id;
    private String name;
    private int age;
    private String profile;
    private String logo;
    private String createdate;
    private String createtime;
    private Timestamp insertdatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Timestamp getInsertdatetime() {
        return insertdatetime;
    }

    public void setInsertdatetime(Timestamp insertdatetime) {
        this.insertdatetime = insertdatetime;
    }

    @Override
    public String getTableName() {
        return "persons_myisam_patition";
    }

    @Override
    public String[] getPrimaryKeys() {
        return new String[]{"id"};
    }

    @Override
    public Boolean getAutoIncrement() {
        return false;
    }
}
