package cn.itxsl.kernel.model.mapped;


import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;

public class ITLabel {

    @Id
    private Long id;

    @Column("belong_id")
    private Long belongId;

    private String lable;

}
