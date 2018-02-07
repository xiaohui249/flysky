package com.sean.mongodb;

import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 14-3-28.
 */
public class SimpleTest {
    public static void main(String[] args) throws Exception {
        Mongo mongo = new Mongo("10.201.91.46", 27017);
        DB db = mongo.getDB("test");
        DBCollection tips = db.getCollection("tips");

//        tips.drop();
//
//        tips = db.getCollection("tips");
//        insert(tips);

        queryAll(tips);

    }

    public static void queryAll(DBCollection collection) {
        long s = System.currentTimeMillis();
        System.out.println("total find : " + collection.getCount());
        DBCursor cursor = collection.find();
        while(cursor.hasNext()) {
            System.out.println(cursor.next());
        }
        System.out.println("read cost: " + (System.currentTimeMillis() - s) + "ms");
    }

    public static void insert(DBCollection collection) {
        List<DBObject> list = new ArrayList<DBObject>();

        long s = System.currentTimeMillis();
        DBObject dbo = null;
        for(int i = 0; i<1000000; i++) {
            dbo = new BasicDBObject();
            dbo.put("ch", "记录" + i);
            dbo.put("py", "jilu" + i);
            dbo.put("count", i);
            list.add(dbo);
        }

        System.out.println("insert: " + collection.insert(list).getN());
        System.out.println("insert cost: " + (System.currentTimeMillis() - s) + "ms");
    }

}
