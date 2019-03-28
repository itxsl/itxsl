package cn.itxsl.kernel.model.oauth;

import lombok.Data;

/**
 * @program: itxsl
 * @description: 码云参数配置
 * @author: itxsl
 * @create: 2019-01-31 14:49
 **/
@Data
public class Gitee {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String accessTokenUri;
    private String userInfoUri;
}
