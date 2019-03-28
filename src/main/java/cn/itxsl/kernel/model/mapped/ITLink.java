package cn.itxsl.kernel.model.mapped;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 15:35
 * @description：友情链接
 */
@Data
@Table("it_link")
public class ITLink extends BaseMapped implements Serializable {

    @Id
    private Long id;

    @Column("title")
    @NotEmpty(message = "网站名称不能为空")
    private String title;

    @Column
    @NotEmpty(message = "网站图标不能为空")
    private String icon;

    @Column
    @NotEmpty(message = "网站地址不能为空")
    private String url;

    @Column
    @NotEmpty(message = "网站简介不能为空")
    @ColDefine(type = ColType.TEXT)
    private String synopsis;

    @Column
    private String icon2;

    @Column
    private String nickname;

    @Column
    private Integer available;

    private String email;

}
