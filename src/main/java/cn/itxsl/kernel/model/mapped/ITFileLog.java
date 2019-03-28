package cn.itxsl.kernel.model.mapped;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;

/**
 * 描述:
 * 文件记录
 *
 * @author itxsl
 * @create 2019-01-18 14:00
 */
@Data
@Table("it_file_log")
public class ITFileLog implements Serializable {

    public ITFileLog() {
    }

    public ITFileLog(Long timestamp, String url) {
        this.timestamp = timestamp;
        this.url = url;
    }

    @Id
    private Long id;

    @Column("belong_type")
    @Comment("1:网站共用，2:网站日记")
    private Integer belongType;

    @Column("belong_id")
    private Long belongId;

    @Column
    private Long timestamp;

    @Column
    private String url;

}