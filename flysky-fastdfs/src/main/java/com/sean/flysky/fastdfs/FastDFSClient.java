package com.sean.flysky.fastdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 该类包含常用的上传、下载、删除等方法
 *
 * @author xiaoh
 * @create 2018-10-16 14:11
 **/
public class FastDFSClient {
    private static Logger logger = LoggerFactory.getLogger(FastDFSClient.class);
    private static TrackerClient trackerClient;
    private static TrackerServer trackerServer;
    private static StorageServer storageServer;
    private static StorageClient storageClient;

    static {
        try {
//            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init("fdfs_client.conf");
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            logger.error("FastDFS Client Init Fail!",e);
        }
    }

    public static String[] upload(FastDFSFile file) {
        logger.info("File Name: " + file.getName() + "File Length:" + file.getContent().length);

        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author", file.getAuthor());

        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        try {
            storageClient = new StorageClient(trackerServer, storageServer);
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (IOException e) {
            logger.error("IO Exception when uploadind the file:" + file.getName(), e);
        } catch (Exception e) {
            logger.error("Non IO Exception when uploadind the file:" + file.getName(), e);
        }
        logger.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

        if (uploadResults == null) {
            logger.error("upload file fail, error code:" + storageClient.getErrorCode());
        }
        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];

        logger.info("upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
        return uploadResults;
    }

    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            storageClient = new StorageClient(trackerServer, storageServer);
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    public static InputStream downFile(String groupName, String remoteFileName) {
        try {
            storageClient = new StorageClient(trackerServer, storageServer);
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    public static void deleteFile(String groupName, String remoteFileName)
            throws Exception {
        storageClient = new StorageClient(trackerServer, storageServer);
        int i = storageClient.delete_file(groupName, remoteFileName);
        logger.info("delete file successfully!!!" + i);
    }
}
