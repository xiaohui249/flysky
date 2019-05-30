package com.sean.flysky.kafka;

import com.alibaba.fastjson.JSONObject;
import com.sean.flysky.utils.sequence.IdWorker;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * ItmTranDelt实体对象工厂
 *
 * @author xiaoh
 * @create 2019-04-11 12:00
 **/
public class ItmTranDeltFactory {

    private final static int BASE_NUM = 1000, BASE_NUM_FOR_COST = 30000, BASE_NUM_RET_CODE = 4000;
    private final static IdWorker worker = new IdWorker();
    private final static List<String> targetIpAndPorts = Arrays.asList("192.168.1.100:8080",
            "192.168.1.101:8080", "192.168.1.102:8080", "192.168.1.100:8081",
            "192.168.1.101:8081", "192.168.1.102:8081");
    private final static List<String> sourceIps = Arrays.asList("10.7.3.130", "10.7.3.131", "10.7.3.132",
            "10.2.168.100", "10.2.168.101", "10.2.168.102", "10.2.168.103", "10.2.168.104");
    private final static List<String> tranNames = Arrays.asList("医保查询", "基本交易查询", "单笔私人查询",
            "委托收款", "查询一卡通帐户", "现金查询", "出对帐单", "网银口头挂失", "开活期账户");
    private final static List<String> channels = Arrays.asList("ATM", "EBANK", "WeChat", "手机银行");

    public static JSONObject createJson() {
        JSONObject ret = new JSONObject();

        ret.put("mon_sid", worker.nextId());

        int targetIndex = new Random().nextInt(BASE_NUM) % targetIpAndPorts.size();
        String[] ipAndPort = targetIpAndPorts.get(targetIndex).split(":");
        ret.put("target_ip", ipAndPort[0]);
        ret.put("target_port", ipAndPort[1]);

        int sourceIndex = new Random().nextInt(BASE_NUM) % sourceIps.size();
        ret.put("source_ip", sourceIps.get(sourceIndex));

        ret.put("tran_timestamp", DateTime.now().toString("YYYY-MM-dd HH:mm:ss.S"));

        int tranNameIndex = new Random().nextInt(BASE_NUM) % tranNames.size();
        ret.put("tran_name", tranNames.get(tranNameIndex));

        int channelIndex = new Random().nextInt(BASE_NUM) % channels.size();
        ret.put("channel", channels.get(channelIndex));

        int cost = new Random().nextInt(BASE_NUM_FOR_COST);
        if(cost < 20) {
            ret.put("tran_cost", 0); //无响应
            ret.put("ret_code", "");
            ret.put("ret_msg", "");
            ret.put("tran_result", "8");
            ret.put("excp_stat", "N");
        } else {
            ret.put("tran_cost", cost);
            int random = new Random().nextInt(BASE_NUM_RET_CODE);
            if(random < 1000) {
                ret.put("ret_code", "0000");
                ret.put("ret_msg", "成功");
                ret.put("tran_result", "0");
                ret.put("excp_stat", "S");
            } else if(random < 2000) {  //1xxx
                ret.put("ret_code", String.valueOf(random));
                ret.put("ret_msg", "程序异常");
                ret.put("tran_result", "1");
                ret.put("excp_stat", "E");
            } else if(random < 3000) {  //2xxx
                ret.put("ret_code", String.valueOf(random));
                ret.put("ret_msg", "业务处理失败");
                ret.put("tran_result", "2");
                ret.put("excp_stat", "S");
            }else { //3xxx
                ret.put("ret_code", String.valueOf(random));
                ret.put("ret_msg", "未知异常");
                ret.put("tran_result", "3");
                ret.put("excp_stat", "S");
            }
        }

        return ret;
    }

}
