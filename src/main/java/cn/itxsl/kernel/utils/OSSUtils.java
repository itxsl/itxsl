package cn.itxsl.kernel.utils;

import cn.itxsl.kernel.model.ITXSL;
import cn.itxsl.kernel.model.mapped.ITFileLog;
import cn.itxsl.kernel.model.oss.OSS;
import cn.itxsl.kernel.repository.FileLogRepository;
import com.aliyun.oss.OSSClient;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 描述:
 * 对象存储工具类
 *
 * @author itxsl
 * @create 2019-01-18 16:52
 */
public class OSSUtils {

    private static Logger logger = LoggerFactory.getLogger(OSSUtils.class);

    private static OSS oss = SpringContextUtils.getBean(OSS.class);

    private static Dao dao = SpringContextUtils.getBean(Dao.class);

    /**
     * 上传文件到阿里云
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static String aliYunUploadFile(MultipartFile multipartFile) throws IOException {
        OSSClient ossClient = new OSSClient(oss.getEndpoint(), oss.getAccessKeyId(), oss.getAccessKeySecret());
        String fileName = getFileName(multipartFile,oss.getPath());
        ossClient.putObject(oss.getBucketName(), fileName, new ByteArrayInputStream(multipartFile.getBytes()));
        ossClient.shutdown();
        return fileName;
    }

    /**
     * 上传文件到腾讯云
     * @param multipartFile
     * @return
     */
    public static String qCloudUploadFile(MultipartFile multipartFile) {
        String fileName = getFileName(multipartFile,oss.getPath());
        COSClient cosClient = getCosClient();
        File file = new File(multipartFile.getOriginalFilename());
        try {
            InputStream ins = multipartFile.getInputStream();
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(oss.getBucketName(), fileName, file);
        cosClient.putObject(putObjectRequest);
        file.delete();
        return fileName;
    }

    public static void   delQCloudFile(String fileName){
        COSClient cosClient = getCosClient();
        cosClient.deleteObject(oss.getBucketName(),fileName);
    }

    private static COSClient getCosClient() {
        COSCredentials cred = new BasicCOSCredentials(oss.getAccessKeyId(), oss.getAccessKeySecret());
        ClientConfig clientConfig = new ClientConfig(new Region(oss.getQcloudRegion()));
        return new COSClient(cred, clientConfig);
    }

    /**
     * 获取文件全路径和名称
     * @param multipartFile
     * @param path
     * @return
     */
    private static String getFileName(MultipartFile multipartFile,String path) {

        String name = multipartFile.getResource().getFilename();
        return path==null?"":path+ UUID.randomUUID().toString().replaceAll("-", "") +name.substring(name.lastIndexOf(".") - 1, name.length());
    }


    /**
     * 更新文件上传记录所属对象
     * @param timestamp
     * @param id
     */
    public static void updateFileLog(Long timestamp, Long id,Integer type) {
        List<ITFileLog> logs = dao.query(ITFileLog.class, Cnd.where("timestamp", "=", timestamp));
        List<ITFileLog> fileLogs = new ArrayList<>();
        for (ITFileLog log : logs) {
            log.setBelongId(id);
            log.setBelongType(type);
            fileLogs.add(log);
        }
        dao.update(fileLogs);
    }

    public static void delFile(String fileName){
        if (ITXSL.ALIYUN_CLOUD.equals(oss.getEnable())){
            logger.error("该图片未删除:{}",fileName);
        }
        if (ITXSL.QQ_CLOUD.equals(oss.getEnable())){
            delQCloudFile(fileName);
            dao.clear(ITFileLog.class,Cnd.where("url","=",fileName));
        }
    }


}