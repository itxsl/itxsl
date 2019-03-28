package cn.itxsl.kernel.utils;

import org.nutz.dao.Cnd;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/15 21:22
 * @description：条件工具类
 */
public class CndUtils {

    public static Cnd cnd(){
        return Cnd.where("1","=",1);
    }

}
