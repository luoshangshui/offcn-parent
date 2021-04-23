package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.ToString;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@Data
@ToString
public class OSSTemplate {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String upload(InputStream inputStream, String fileName) {
        //1,文件上传
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String folderName = format.format(new Date());
        fileName = UUID.randomUUID().toString().replaceAll("-", "") + "" + fileName;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(bucketName, "pic/" + folderName + "/" + fileName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        String url = "http://" + bucketName + endpoint + "/pic/" + folderName + "/" + fileName;
        System.out.println("上传成功");
        return url;
    }
}
