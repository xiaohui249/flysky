package com.sean.flysky.pojo;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;
import java.time.Instant;

/**
 * 交易流水对象
 *
 * @author xiaoh
 * @create 2019-04-09 18:25
 **/
@Measurement(name = "trans")
public class ItmTranDelt implements Serializable{
    @Column(name = "mon_sid")
    private String monSid;
    @Column(name = "target_ip", tag = true)
    private String targetIp;
    @Column(name = "target_port", tag = true)
    private String targetPort;
    @Column(name = "source_ip", tag = true)
    private String sourceIp;
    @Column(name = "time")
    private Instant tranTimeStamp;
    @Column(name = "tran_name", tag = true)
    private String tranName;
    @Column(name = "channel", tag = true)
    private String channel;
    @Column(name = "tran_cost")
    private Integer tranCost;
    @Column(name = "ret_code", tag = true)
    private String retCode;
    @Column(name = "ret_msg")
    private String retMsg;
    @Column(name = "tran_result", tag = true)
    private String tranResult;
    @Column(name = "excp_stat", tag = true)
    private String excpStat;

    public String getMonSid() {
        return monSid;
    }

    public void setMonSid(String monSid) {
        this.monSid = monSid;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public String getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(String targetPort) {
        this.targetPort = targetPort;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public Instant getTranTimeStamp() {
        return tranTimeStamp;
    }

    public void setTranTimeStamp(Instant tranTimeStamp) {
        this.tranTimeStamp = tranTimeStamp;
    }

    public String getTranName() {
        return tranName;
    }

    public void setTranName(String tranName) {
        this.tranName = tranName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getTranCost() {
        return tranCost;
    }

    public void setTranCost(Integer tranCost) {
        this.tranCost = tranCost;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getTranResult() {
        return tranResult;
    }

    public void setTranResult(String tranResult) {
        this.tranResult = tranResult;
    }

    public String getExcpStat() {
        return excpStat;
    }

    public void setExcpStat(String excpStat) {
        this.excpStat = excpStat;
    }
}
