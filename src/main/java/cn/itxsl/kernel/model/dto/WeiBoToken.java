package cn.itxsl.kernel.model.dto;

import lombok.Data;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/19 11;
 * @description：微博token
 */
@Data
public class WeiBoToken {

    private String access_token;
    private Long remind_in;
    private Long expires_in;
    private String uid;
    private Boolean isRealName;
}
