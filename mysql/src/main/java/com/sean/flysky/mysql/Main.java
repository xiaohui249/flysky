package com.sean.flysky.mysql;

import com.sean.flysky.mysql.dbutil.DbHelper;
import com.sean.flysky.mysql.model.Persons;
import com.sean.flysky.mysql.sequence.IdWorker;
import com.xiaoleilu.hutool.setting.Setting;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 主函数
 *
 * @author xiaoh
 * @create 2018-08-11 11:02
 **/
public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);
    private final static IdWorker idWorker;
    private final static Setting setting;
    private final static String profile = "His handsome profile was turned away from us.Profiles将stereotypes(版型)、tagged values(标记值)" +
            "和constraints(约束)应用于具体的模型元素比如类、属性、操作和活动。" +
            "一个Profile对象就是一系列为特定领域(比如，航空航天、保健、金融)或平台(J2EE、.NET)自定义的UML集合。";
    private final static String logo = "If an internal link led you here, you may wish to change the link to point directly to the intended article." +
            "帮助你分析并发现程序运行的瓶颈，找到耗时所在，同时也能帮助你发现不会被执行的代码。从而最终实现程序的优化。";

    /**
     * 根据配置文件初始化ID生成器
     */
    static  {
        setting = new Setting("app.properties");
        long workerId = setting.getLong("workerId", "IdWorker", 0L);
        long datacenterId = setting.getLong("datacenterId", "IdWorker", 0L);
        idWorker = new IdWorker(workerId, datacenterId);
        logger.info("序列生成器IdWorker初始化成功！");
    }

    public static void main(String[] args) throws Exception{
        int batchSie = setting.getInt("batchSize", "sql", 1000);
        List<Persons> personsList = new ArrayList<>(batchSie);
        int total = Integer.MAX_VALUE;
        int gap = 500000;
        int i=0;
        long s = System.currentTimeMillis();
        while(i <= total) {
            personsList.add(createPersons());
            i++;
            if(personsList.size() == batchSie) {
                DbHelper.batchInsert(personsList);
                personsList.clear();
            }

            if(gap > 0 && (i % gap == 0)) { //  如果已经插入50w，则休眠4分钟，约每5min中50w笔记录
                logger.debug("准备休眠4min...");
                Thread.sleep(1000 * 60 * 4);
            }
        }
        logger.debug("insert {} records, cost {} ms", i, System.currentTimeMillis() -s);
    }

    public static Persons createPersons() {
        Persons persons = new Persons();
        persons.setId(String.valueOf(idWorker.nextId()));
        persons.setName("andy");
        persons.setAge(RandomUtils.nextInt(10, 150));
        persons.setProfile(profile);
        persons.setLogo(logo);
        DateTime now = new DateTime();
//        persons.setCreatedate(now.toString("yyyy-MM-dd"));
//        persons.setCreatetime(now.toString("HH:mm:ss"));
        persons.setCreatedate(now.toString("yyyyMMdd"));
        persons.setCreatetime(now.toString("HHmmssSSS"));
        return persons;
    }
}
