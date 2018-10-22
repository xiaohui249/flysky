package com.sean.flysky.fastdfs;

import org.csource.fastdfs.FileInfo;

import java.io.*;

/**
 * FastDFSClient测试类
 *
 * @author xiaoh
 * @create 2018-10-22 16:20
 **/
public class FastDFSClientTest {
    public static void main(String[] args) throws Exception {
        File file = new File("E:/外贸管理台开发计划表-20181011.xlsx");
        String fileName = file.getName();
        FastDFSFile dfsFile = new FastDFSFile();
        dfsFile.setName(fileName);
        dfsFile.setAuthor("sean");
        dfsFile.setExt(fileName.substring(fileName.lastIndexOf(".")));
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] content = new byte[fileInputStream.available()];
        fileInputStream.read(content);
        fileInputStream.close();
        dfsFile.setContent(content);

        // ---文件上传---
        String[] ret = FastDFSClient.upload(dfsFile);
        System.out.println("上传返回结果信息数量：" + ret.length);
        String groupName = ret[0];
        String remoteFileName = ret[1];
        System.out.println("groupName：" + groupName + ", remoteFileName: " + remoteFileName);

        // --- 查询文件信息---
        FileInfo fileInfo = FastDFSClient.getFile(groupName, remoteFileName);

        // ---文件下载---
        InputStream inputStream = FastDFSClient.downFile(groupName, remoteFileName);
        FileOutputStream out = new FileOutputStream("E:\\save_" + fileName);
        //创建一个缓冲区
        byte buffer[] = new byte[1024];
        //判断输入流中的数据是否已经读完的标致
        int len = 0;
        while((len = inputStream.read(buffer)) > 0){
            out.write(buffer, 0, len);
        }
        //关闭输出流
        out.close();
        //关闭输入流
        inputStream.close();

        // ---文件删除---
        FastDFSClient.deleteFile(groupName, remoteFileName);
    }
}
