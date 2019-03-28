package cn.itxsl.kernel.model.mapped;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/20 16:42
 * @description：邮件
 */
@Data
@Table("it_email")
public class ITEmail extends BaseMapped {

    @Id
    private Long id;

    @Column
    private String email;

    @Column
    private String subject;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String content;

    @Column("status")
    private String status;

}
