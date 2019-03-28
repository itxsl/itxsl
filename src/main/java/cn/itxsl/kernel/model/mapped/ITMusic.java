package cn.itxsl.kernel.model.mapped;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import javax.validation.constraints.NotEmpty;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 13:47
 * @description：音乐
 */
@Data
@Table("it_music")
public class ITMusic extends BaseMapped {



    @Id
    private Long id;

    @Column
    @NotEmpty(message = "音乐名称不能为空")
    private String name;

    @Column
    @NotEmpty(message = "音乐封面不能为空")
    private String cover;

    @Column
    @NotEmpty(message = "音乐作者不能为空")
    private String singer;

    @Column
    @NotEmpty(message = "音乐地址不能为空")
    private String url;

}
