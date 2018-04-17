package com.sean.flysky.utils.pcap;

/**
 * IP 数据报头
 *
 * @author xiaoh
 * @create 2018-04-16 17:55
 **/
public class IPHeader {
    /**
     * 协议版本号(4 bit)及包头长度(4bit) =（1 字节）
     * 版本号(Version):一般的值为0100（IPv4），0110（IPv6）
     * IP包头最小长度为20字节
     */
    private byte varHLen;

    /**
     * Type of  Service：服务类型，（1 字节）
     */
    private byte tos;

    /**
     * 总长度（2 字节）
     */
    private short totalLen;

    /**
     * 标识（2 字节）
     */
    private short id;

    /**
     * 标志与偏移量（2 字节）
     */
    private short flagSegment;

    /**
     * Time to Live：生存周期（1 字节）
     */
    private byte ttl;

    /**
     * 协议类型（1 字节）
     */
    private byte protocol;

    /**
     * 头部校验和（2 字节）
     */
    private short checkSum;

    /**
     * 源 IP（4 字节）
     */
    private int srcIP;

    /**
     * 目的 IP（4 字节）
     */
    private int dstIP;

    public byte getVarHLen() {
        return varHLen;
    }

    public void setVarHLen(byte varHLen) {
        this.varHLen = varHLen;
    }

    public byte getTos() {
        return tos;
    }

    public void setTos(byte tos) {
        this.tos = tos;
    }

    public short getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(short totalLen) {
        this.totalLen = totalLen;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public short getFlagSegment() {
        return flagSegment;
    }

    public void setFlagSegment(short flagSegment) {
        this.flagSegment = flagSegment;
    }

    public byte getTtl() {
        return ttl;
    }

    public void setTtl(byte ttl) {
        this.ttl = ttl;
    }

    public byte getProtocol() {
        return protocol;
    }

    public void setProtocol(byte protocol) {
        this.protocol = protocol;
    }

    public short getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(short checkSum) {
        this.checkSum = checkSum;
    }

    public int getSrcIP() {
        return srcIP;
    }

    public void setSrcIP(int srcIP) {
        this.srcIP = srcIP;
    }

    public int getDstIP() {
        return dstIP;
    }

    public void setDstIP(int dstIP) {
        this.dstIP = dstIP;
    }

    public IPHeader() { }

    @Override
    public String toString() {
        return "IPHeader [varHLen=" + Integer.toHexString(varHLen).toUpperCase()
                + ", tos=" + Integer.toHexString(tos).toUpperCase()
                + ", totalLen=" + totalLen
                + ", id=" + Integer.toHexString(id).toUpperCase()
                + ", flagSegment=" + Integer.toHexString(flagSegment).toUpperCase()
                + ", ttl=" + ttl
                + ", protocol=" + protocol
                + ", checkSum=" + Integer.toHexString(checkSum).toUpperCase()
                + ", srcIP=" + Integer.toHexString(srcIP).toUpperCase()
                + ", dstIP=" + Integer.toHexString(dstIP).toUpperCase()
                + "]";
    }
}
