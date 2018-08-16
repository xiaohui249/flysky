package com.sean.flysky.mysql.dbutil;

import java.lang.reflect.Field;

/**
 * 基础类对象
 * Created by sean on 2016/6/24.
 */
public abstract class BaseModel {

    protected String tableName = "";
    protected String[] fields = new String[]{};
    protected String[] primaryKeys = new String[]{};
    protected Boolean autoIncrement = false;

    /**
     * 获取bean对象的字段名(不改变命名规则)，
     * 用于生成insert语句
     * @return
     * 
     * @author xiamh
     * @date   2016年8月17日 上午10:52:02
     */
    public String[] getFields() {
        Field[] fieldArray = this.getClass().getDeclaredFields();
        fields = new String[fieldArray.length];
        for(int i=0; i<fieldArray.length; i++) {
            fields[i] = fieldArray[i].getName();
        }
        return fields;
    }
    
    /**
     * 用于生成get／set方法
     * @return
     * 
     * @author xiamh
     * @date   2016年8月17日 上午10:58:56
     */
    public String[] getParamFields(){
    	Field[] fieldArray = this.getClass().getDeclaredFields();
        fields = new String[fieldArray.length];
        for(int i=0; i<fieldArray.length; i++) {
            fields[i] = fieldArray[i].getName();
        }
        return fields;
    }

    public abstract String getTableName();

    public abstract String[] getPrimaryKeys();

    public abstract Boolean getAutoIncrement();

}
