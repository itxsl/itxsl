package cn.itxsl.kernel.model.oauth;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/19 9:42
 * @description：微博
 */

import lombok.Data;

@Data
public class WeiBo{
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String accessTokenUri;
    private String userInfoUri;

}