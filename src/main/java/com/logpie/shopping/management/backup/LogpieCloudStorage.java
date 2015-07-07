// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * @author zhoyilei
 *
 */
public class LogpieCloudStorage
{
    private static final Logger LOG = Logger.getLogger(LogpieCloudStorage.class);

    private static final String sAccessKeyId = "hxc5FNpJCVTeSRgF";
    private static final String sAccessKeySecret = "5cJPfyVOIjDqIgLD6kzxKlAkvsjq3J";
    private static final String sAliyunEndpoint = "http://oss-cn-qingdao.aliyuncs.com";

    private OSSClient mOssClient;

    public LogpieCloudStorage()
    {
        // 初始化一个OSSClient
        mOssClient = new OSSClient(sAliyunEndpoint, sAccessKeyId, sAccessKeySecret);
    }

    public List<String> getFileKeysInBucket(final String bucketName)
    {
        // 构造 ListObjectsRequest 请求
        final ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        // listObjectsRequest.setPrefix("/");
        // Set the return keys size. The maximum is 1000 (almost 3 years).
        listObjectsRequest.setMaxKeys(1000);
        final ObjectListing listing = mOssClient.listObjects(listObjectsRequest);
        final List<String> fileKeys = new ArrayList<String>();
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries())
        {
            fileKeys.add(objectSummary.getKey());
        }
        return fileKeys;
    }

    public void uploadFile(final String bucketName, final String key, final String filePath)
    {
        // 获取指定文件的输入流
        final File file = new File(filePath);
        try
        {
            final InputStream content = new FileInputStream(file);

            // 创建上传Object的Metadata
            final ObjectMetadata meta = new ObjectMetadata();

            // 必须设置ContentLength
            meta.setContentLength(file.length());

            // 上传Object.
            final PutObjectResult result = mOssClient.putObject(bucketName, key, content, meta);

            // 打印ETag
            LOG.debug(result.getETag());
        } catch (FileNotFoundException e)
        {
            LOG.error("file doesn't exist!", e);
        }

    }

}
