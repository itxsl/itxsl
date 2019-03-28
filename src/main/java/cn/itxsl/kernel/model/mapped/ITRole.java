package cn.itxsl.kernel.model.mapped;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 描述:
 * 角色
 *
 * @author itxsl
 * @create 2018-12-14 16:48
 */
@Data
@Table("it_role")
public class ITRole extends BaseMapped implements GrantedAuthority, Serializable {

    @Id
    private Long id;

    @Name
    @NotEmpty
    private String role;

    @Column
    @NotEmpty
    private String explain;

    @Override
    public String getAuthority() {
        return role;
    }

}
