package cn.itxsl.kernel.repository;

import cn.itxsl.kernel.model.mapped.ITCommon;
import cn.itxsl.kernel.model.mapped.ITUser;
import cn.itxsl.kernel.repository.base.Repository;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserRepository extends Repository<ITUser> implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public ITUser loadUserByUsername(String username) throws UsernameNotFoundException {
       ITUser user = get(Cnd.where("username", "=", username));
        if (user==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        user = dao.fetchLinks(user, "authorities");
        logger.info("开始登录:{}", Json.toJson(user));
        return user;
    }
}
