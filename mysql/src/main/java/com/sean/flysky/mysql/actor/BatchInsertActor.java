package com.sean.flysky.mysql.actor;

import akka.actor.UntypedActor;
import com.sean.flysky.mysql.dbutil.DbHelper;
import com.sean.flysky.mysql.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量插入Actor
 *
 * @author xiaoh
 * @create 2018-08-16 10:39
 **/
public class BatchInsertActor extends UntypedActor {
    private final static Logger logger = LoggerFactory.getLogger(BatchInsertActor.class);
    private List<Persons> list = new ArrayList<>(1000);

    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof Persons) {
            list.add((Persons) o);
            if (list.size() == 1000) {
                logger.info("receive message size: " + list.size());
                DbHelper.batchInsert(list);
                list.clear();
            }
        } else if(o.toString().equals("over")) {
            logger.info("over!!! receive message size: " + list.size());
            DbHelper.batchInsert(list);
            list.clear();
            getContext().stop(getSelf());
        } else {
            unhandled(o);
        }
    }
}
