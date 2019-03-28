package cn.itxsl.kernel.model.dto;

import cn.itxsl.kernel.model.mapped.ITRole;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Name;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/17 21:35
 * @description：用户
 */
@Data
public class ITUserDTO {

    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String sex;

    private String phone;

    private String email;

    private String icon;

    private String type;

    private boolean lock;

    private String role;

}
