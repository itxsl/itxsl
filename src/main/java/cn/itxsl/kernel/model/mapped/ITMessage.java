package cn.itxsl.kernel.model.mapped;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 13:50
 * @description：留言
 */
@Data
@Table("it_message")
public class ITMessage extends BaseMapped implements Serializable {

    @Id
    private Long id;

    @Column("parent_id")
    private Long parentId=0L;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String icon;

    @Column
    private String nickname;

    @Column
    @NotEmpty(message = "留言不能为空")
    @ColDefine(type = ColType.TEXT)
    private String message;

    @Column
    private Integer available;

    private List<ITMessage> childs;

    private String email;


}
