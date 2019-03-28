package cn.itxsl.kernel.model.oauth;

import lombok.Data;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/19 9:42
 * @description：QQ
 */
@Data
public class QQ{

    private String openid;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String code_uri;
    private String access_token_uri;
    private String openid_uri;
    private String user_info_uri;

}
