package cn.itxsl.kernel.model.email;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/20 16:46
 * @description：邮箱配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class Email {

    private String username;

    private String password;

    private String host;

    private Integer port;

    private String code;

    private String auth;

    private String authKey;

    private String timeout;

    private String timeoutkey;

    private String loglevel;

    private String loglevelkey;

    private String protocol;

    private String redirectUrl;
}
