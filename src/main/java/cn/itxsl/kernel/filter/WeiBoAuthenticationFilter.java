package cn.itxsl.kernel.filter;

import cn.itxsl.kernel.model.dto.WeiBoDTO;
import cn.itxsl.kernel.model.dto.WeiBoToken;
import cn.itxsl.kernel.model.mapped.ITRole;
import cn.itxsl.kernel.model.mapped.ITUser;
import cn.itxsl.kernel.model.oauth.OAuth2;
import cn.itxsl.kernel.utils.SpringContextUtils;
import cn.itxsl.kernel.utils.StrUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.http.Http;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

import static cn.itxsl.kernel.utils.StrUtils.NotEmpty;
import static cn.itxsl.kernel.utils.StrUtils.getIcon;

public class WeiBoAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Dao dao = SpringContextUtils.getBean(Dao.class);

    private OAuth2 oAuth2 = SpringContextUtils.getBean(OAuth2.class);

    public WeiBoAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, "GET"));
    }

    public WeiBoAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        OAuth2 auth = SpringContextUtils.getBean(OAuth2.class);
        HashMap<String, Object> params = new HashMap<>();
        params.put("client_id", auth.getWeibo().getClientId());
        params.put("client_secret", auth.getWeibo().getClientSecret());
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", auth.getWeibo().getRedirectUri());
        params.put("code", request.getParameter("code"));
        logger.info(Json.toJson(params));
        String content = Http.post(auth.getWeibo().getAccessTokenUri(), params, 2000);
        WeiBoToken weiBoToken = Json.fromJson(WeiBoToken.class, content);
        return authenticationManager().authenticate(new UsernamePasswordAuthenticationToken(weiBoToken.getAccess_token(), weiBoToken.getUid()));
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                if (authentication.getName() != null && authentication.getCredentials() != null) {
                    dao = SpringContextUtils.getBean(Dao.class);
                    Date date = new Date();
                    ITUser existUser = dao.fetch(ITUser.class, Cnd.where("username", "=", authentication.getCredentials()));
                    if (existUser == null) {
                        HashMap<String, Object> params = new HashMap<>();
                        params.clear();
                        params.put("access_token", authentication.getName());
                        params.put("uid", authentication.getCredentials());
                        String userContent = Http.get(oAuth2.getWeibo().getUserInfoUri(), params, 2000).getContent();
                        logger.info("微博:{}",Json.toJson(userContent));
                        WeiBoDTO weiBoDTO = Json.fromJson(WeiBoDTO.class, userContent);
                        ITUser user = new ITUser();
                        user.setNickname(weiBoDTO.getScreen_name());
                        user.setUsername(authentication.getCredentials().toString());
                        user.setPassword(StrUtils.getPassword(authentication.getCredentials().toString()));
                        user.setAccountNonLocked(true);
                        user.setSex(NotEmpty(weiBoDTO.getGender())?("m".equals(weiBoDTO.getGender())?"男":"女"):"未知");
                        user.setIcon(getIcon(weiBoDTO.getAvatar_large()));
                        user.setCreateTime(date);
                        user.setUpdateTime(date);
                        user.setType("微博第三方");
                        user.setCreateUsername("微博第三方");
                        user.setUpdateUsername("微博第三方");
                        dao.insert(user).setAuthorities(dao.query(ITRole.class, Cnd.where("id", "=", 2)));
                        return new UsernamePasswordAuthenticationToken(dao.insertRelation(user, "authorities"), user.getAuthorities(), user.getAuthorities());
                    } else {
                        existUser.setUpdateTime(date);
                        dao.updateIgnoreNull(existUser);
                        existUser = dao.fetchLinks(existUser, "authorities");
                        return new UsernamePasswordAuthenticationToken(existUser, existUser.getAuthorities(), existUser.getAuthorities());
                    }

                }
                throw new BadCredentialsException("认证失败");
            }
        };
    }


}