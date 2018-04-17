package com.sean.flysky.utils.pcap;

import java.util.List;

/**
 * Pcap 结构
 *
 * @author xiaoh
 * @create 2018-04-16 18:01
 **/
public class PcapStruct {
    private PcapFileHeader fileHeader;
    private List<PcapDataHeader> dataHeaders;

    public PcapFileHeader getFileHeader() {
        return fileHeader;
    }
    public void setFileHeader(PcapFileHeader fileHeader) {
        this.fileHeader = fileHeader;
    }
    public List<PcapDataHeader> getDataHeaders() {
        return dataHeaders;
    }
    public void setDataHeaders(List<PcapDataHeader> dataHeaders) {
        this.dataHeaders = dataHeaders;
    }

    public PcapStruct() {}
}
