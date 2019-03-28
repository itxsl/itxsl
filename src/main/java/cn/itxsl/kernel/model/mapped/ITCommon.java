package cn.itxsl.kernel.model.mapped;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/15 21:06
 * @description：网站公用部分
 */
@Data
@Table("it_common")
public class ITCommon extends BaseMapped implements Serializable {

    @Id
    private Long id;

    @Column
    @Comment("网站图标")
    @NotEmpty(message = "网站Logo不能为空")
    private String logo;

    @Column("logo_name")
    @Comment("网站名称")
    @NotEmpty(message = "网站名称不能为空")
    private String logoName;

    @Column("visitor_volume")
    @Comment("网站访问量")
    private Long visitorVolume;

    @Column("update_date")
    @Comment("网站最后更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    @Column("record_info")
    @Comment("备案号")
    @NotEmpty(message = "网站备案号不能为空")
    private String recordInfo;

    @Column("keyword")
    @Comment("关键字")
    @ColDefine(type = ColType.TEXT)
    private String keyword;

    @Column("description")
    @Comment("描述")
    @ColDefine(type = ColType.TEXT)
    private String description;

    @Column("index_background")
    @Comment("首页壁纸")
    @NotEmpty(message = "网站首页壁纸不能为空")
    private String indexBackground;

    @Column("index_title")
    @Comment("首页题头")
    @NotEmpty(message = "网站首页题头不能为空")
    private String indexTitle;

    @Column("index_title_content")
    @ColDefine(type = ColType.TEXT)
    @Comment("首页题头内容")
    @NotEmpty(message = "网站首页题头内容不能为空")
    private String indexTitleContent;

    @Column("index_welcome")
    @Comment("首页欢迎语")
    @NotEmpty(message = "网站首页欢迎语不能为空")
    @ColDefine(type = ColType.TEXT)
    private String indexWelcome;

    @Column("user_info")
    @Comment("个人信息")
    @NotEmpty(message = "网站个人信息不能为空")
    @ColDefine(type = ColType.TEXT)
    private String userInfo;

    @Column("user_skill")
    @Comment("个人技能")
    @NotEmpty(message = "网站个人技能不能为空")
    @ColDefine(type = ColType.TEXT)
    private String userSkill;

    @Column("user_contact")
    @Comment("个人联系方式")
    @NotEmpty(message = "网站个人联系方式不能为空")
    @ColDefine(type = ColType.TEXT)
    private String userContact;

}
