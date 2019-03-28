package cn.itxsl.kernel.model.mapped;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/20 12:37
 * @description：评论管理
 */
@Data
@Table("it_comment")
public class ITComment extends BaseMapped {

    @Id
    private Long id;

    @Column("parent_id")
    private Long parentId=0L;

    @NotNull(message = "所属文章id不能为空")
    @Column("belong_id")
    private Long belongId;

    @Column
    @ColDefine(type = ColType.TEXT)
    @NotEmpty(message = "内容不能为空")
    private String content;

    @Column
    private String icon;

    @Column
    private String nickname;

    @Email(message = "邮箱格式有误")
    private String email;

    @Column
    private Integer available;

    private List<ITComment> child;


}
