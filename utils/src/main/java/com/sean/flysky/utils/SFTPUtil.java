package com.sean.flysky.utils;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Administrator on 2017/11/16.
 */
public class SFTPUtil {
    private static String host = "127.0.0.1";//地址
    private static int port = 22;//端口号
    private static String username = "kayak";//用户名
    private static String password = "kayak";//密码
    private static String directory = "/home/kayak";//文件路径
    private static Session sshSession = null;
    public static void main(String[] args) throws Exception {
        String downloadFile = "test";//要下载的文件名
        String saveFile = "E:/test";//下载的文件路径
        ChannelSftp sftp = SFTPUtil.connect(host, port, username, password);

        Vector<Object> files = SFTPUtil.listFiles(directory, sftp);
        System.out.printf("file size: " + files.size());
        for(Object file : files) {
            ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) file;
            System.out.println(entry.getFilename());
        }

//        com.SFTPUtil.download(directory, downloadFile, saveFile, sftp);
        sftp.disconnect();//关闭连接
        sshSession.disconnect();//关闭会话
        System.out.println("=====over====");
    }

    /**
     * 连接sftp服务器
     * @param host  主机
     * @param port  端口
     * @param username  用户名
     * @param password    密码
     * @return
     */
    public static ChannelSftp connect(String host, int port, String username,String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            //System.out.println("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            //System.out.println("Session connected.");
            //System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + " "+port);
        } catch (Exception e) {
            System.out.println(e);
        }
        return sftp;
    }
    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile  要上传的文件
     * @param sftp
     */
    public static void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
     * 下载文件
     *
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile  存在本地的路径
     * @param sftp
     */
    public static void download(String directory, String downloadFile,String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            FileOutputStream fo =  new FileOutputStream(file);
            sftp.get(downloadFile, fo);
            fo.close();
            System.out.println("down file:"+downloadFile);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void download(String downloadFile) {
        ChannelSftp sftp = SFTPUtil.connect(host, port, username, password);
        download(directory, downloadFile, "E:/"+downloadFile, sftp);
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile  要删除的文件
     * @param sftp
     */
    public static void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
     * 列出目录下的文件
     *
     * @param directory      要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public static Vector<Object> listFiles(String directory, ChannelSftp sftp)throws SftpException {
        return sftp.ls(directory);
    }
}
