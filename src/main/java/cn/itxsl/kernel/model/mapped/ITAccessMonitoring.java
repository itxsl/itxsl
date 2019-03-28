package cn.itxsl.kernel.model.mapped;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Data
@Table("it_access_monitoring")
public class ITAccessMonitoring {

    public ITAccessMonitoring() {
    }

    public ITAccessMonitoring(String host, Date date) {
        this.host = host;
        this.date = date;
    }

    @Id
    private Integer id;

    @Column
    private String host;

    @Column
    private Date date;


}
