package cn.itxsl.kernel.utils;

import cn.itxsl.kernel.model.mapped.ITUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 13:59
 * @description：运行时工具类
 */
public class RunUtils {

    private static final String NOT_LOGIN = "anonymousUser";

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static ITUser getCurrentUser() {
        if (isLogin()) {
            return (ITUser) getAuthentication().getPrincipal();
        }
        throw new UsernameNotFoundException("未找到用户信息");
    }

    public static boolean isLogin() {
        if (getAuthentication() == null) {
            return false;
        }
        if (NOT_LOGIN.equals(getAuthentication().getPrincipal())) {
            return false;
        }
        if (getAuthentication().isAuthenticated() == false) {
            return false;
        }
        return true;
    }


}
