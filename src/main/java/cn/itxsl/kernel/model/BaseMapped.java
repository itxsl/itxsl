package cn.itxsl.kernel.model;


import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;

import java.util.Date;

@Data
public class BaseMapped {

    @Column("create_username")
    @Comment("创建人")
    private String createUsername;

    @Column("update_username")
    @Comment("更新人")
    private String updateUsername;

    @Column("create_time")
    @Comment("创建时间")
    private Date createTime;

    @Column("update_time")
    @Comment("创建时间")
    private Date updateTime;

}
