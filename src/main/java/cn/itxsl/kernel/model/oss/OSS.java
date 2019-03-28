package cn.itxsl.kernel.model.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 存储对象
 *
 * @author itxsl
 * @create 2019-01-17 13:22
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.oss")
public class OSS {

    //选择是启用腾讯对象存储还是阿里云对象存储
    private String enable;

    //存储服务器域名
    private String endpoint;

    //桶名称
    private String bucketName;

    //账号
    private String accessKeyId;

    //密钥
    private String accessKeySecret;

    //腾讯对象存储所属区域
    private String qcloudRegion;

    //文件存放文件夹
    private String path;

}