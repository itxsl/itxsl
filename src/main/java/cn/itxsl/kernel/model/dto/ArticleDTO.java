package cn.itxsl.kernel.model.dto;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 12:52
 * @description：日记本
 * @modified By：
 * @version: $
 */
@Data
public class ArticleDTO {

    private String id;

    private String title;

    private String cover;

    public ArticleDTO() {
    }

    public ArticleDTO(String id, String title, String cover) {
        this.id = id;
        this.title = title;
        this.cover = cover;
    }
}
