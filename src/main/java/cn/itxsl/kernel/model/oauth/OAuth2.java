package cn.itxsl.kernel.model.oauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/18 23:21
 * @description：三方
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.oauth")
public class OAuth2 {

    private QQ qq = new QQ();

    private WeiBo weibo = new WeiBo();

    private Gitee gitee = new Gitee();

}
