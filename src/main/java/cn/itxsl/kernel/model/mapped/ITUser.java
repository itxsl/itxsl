package cn.itxsl.kernel.model.mapped;

import cn.itxsl.kernel.model.BaseMapped;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.nutz.dao.entity.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 描述:
 * 用户
 *
 * @author itxsl
 * @create 2018-12-14 16:48
 */
@Data
@Table("it_user")
public class ITUser extends BaseMapped implements UserDetails, Serializable {

    @Id
    private Long id;

    @Name
    @NotEmpty(message = "用户名不能为空！")
    private String username;

    @Column
    @NotEmpty(message = "昵称不能为空！")
    private String nickname;

    @Column
    @Range(min = 6, max = 16, message = "密码长度必须6-16位之间！")
    private String password;

    @Column
    private String sex;

    @Column
    @Pattern(regexp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message = "手机号格式错误！")
    private String phone;

    @Column
    @Email(message = "邮箱格式错误！")
    private String email;

    @Column
    private String icon;

    @Column
    private String type;

    @Column
    private boolean isAccountNonLocked;

    @ManyMany(relation = "it_user_role", from = "u_id", to = "r_id")
    private List<ITRole> authorities;


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<ITRole> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setAuthorities(List<ITRole> authorities) {
        this.authorities = authorities;
    }
}
