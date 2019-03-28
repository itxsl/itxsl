package cn.itxsl.kernel.model.mapped;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.nutz.dao.entity.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 12:52
 * @description：日记本
 * @modified By：
 * @version: $
 */
@Data
@Table("it_article")
public class ITArticle extends BaseMapped  implements Serializable {

    @Id
    private Long id;

    @Column
    @Length(min = 1,max = 50,message = "文章标题不能为空，长度不能大于50")
    private String title;

    @Column
    @Length(min = 1,max = 120,message = "文章封面不能为空，长度不能大于120")
    private String cover;

    @Column
    @NotEmpty(message = "文章类型不能为空")
    private String type;

    @Column
    @Length(min = 1,max = 120,message = "文章简介不能为空")
    private String synopsis;

    @Column
    private String music;

    @Column
    @NotEmpty(message = "文章来源不能为空")
    private String original;

    @Column
    @ColDefine(type = ColType.TEXT)
    @NotEmpty(message = "文章内容不能为空")
    private String content;

    @Column("content_type")
    @Range(min =0,max = 2,message = "文章内容类型不能为空[0,1]")
    private Integer contentType;

    @Column
    private Long click;

    @Column
    private String icon;

    @Column
    private String nickname;


    @Column
    @NotNull(message = "文章是否发表不能为空")
    private Integer available;

    @NotNull
    private Long timestamp;

}
