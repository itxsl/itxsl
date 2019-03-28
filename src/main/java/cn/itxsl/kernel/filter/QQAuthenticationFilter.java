package cn.itxsl.kernel.filter;

import cn.itxsl.kernel.model.dto.QQDTO;
import cn.itxsl.kernel.model.dto.QQToken;
import cn.itxsl.kernel.model.mapped.ITRole;
import cn.itxsl.kernel.model.mapped.ITUser;
import cn.itxsl.kernel.model.oauth.OAuth2;
import cn.itxsl.kernel.model.oauth.QQ;
import cn.itxsl.kernel.utils.SpringContextUtils;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.itxsl.kernel.utils.StrUtils.NotEmpty;
import static cn.itxsl.kernel.utils.StrUtils.getIcon;

public class QQAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Dao dao = SpringContextUtils.getBean(Dao.class);
    private OAuth2 oAuth2 = SpringContextUtils.getBean(OAuth2.class);

    public QQAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, "GET"));
    }

    public QQAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String code = request.getParameter("code");
        QQ qq = oAuth2.getQq();
        QQToken qqToken = getToken(code);
        String openid = getOpenid(qqToken);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(qqToken.getAccess_token(), openid);
        return authenticationManager().authenticate(token);
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication auth) throws AuthenticationException {
                if (auth.getName() != null && auth.getCredentials() != null) {
                    Date date = new Date();
                    ITUser existUser = dao.fetch(ITUser.class, Cnd.where("username", "=", auth.getCredentials()));
                    if (existUser == null) {
                        QQ qq = oAuth2.getQq();
                        QQDTO qqdto = getQQUserInfo(auth.getName(), (String) auth.getCredentials());
                        ITUser user = new ITUser();
                        String openid = (String) auth.getCredentials();
                        user.setNickname(qqdto.getNickname());
                        user.setUsername(openid);
                        user.setPassword(getPassword(openid));
                        user.setAccountNonLocked(true);
                        user.setSex(qqdto.getGender());
                        user.setIcon(getIcon(qqdto.getFigureurl_2()));
                        user.setCreateTime(date);
                        user.setUpdateTime(date);
                        user.setType("QQ第三方");
                        user.setCreateUsername("QQ第三方");
                        user.setUpdateUsername("QQ第三方");
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

    private QQToken getToken(String code) {
        QQ qq = oAuth2.getQq();
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", qq.getRedirect_uri());
        params.put("client_id", qq.getClient_id());
        params.put("client_secret", qq.getClient_secret());
        String content = Http.get(qq.getAccess_token_uri(), params, 1000).getContent();
        String[] strings = content.split("&");
        if (strings.length == 3) {
            QQToken qqToken = new QQToken();
            String accessToken = strings[0].replace("access_token=", "");
            Long expiresIn = Long.valueOf(strings[1].replace("expires_in=", ""));
            String refreshToken = strings[2].replace("refresh_token=", "");
            qqToken.setAccess_token(accessToken);
            qqToken.setExpiresIn(expiresIn);
            qqToken.setRefresh_token(refreshToken);
            return qqToken;
        }
        return null;
    }

    private String getOpenid(QQToken qqToken) {
        QQ qq = oAuth2.getQq();
        String content = Http.get(qq.getOpenid_uri() + "?access_token=" + qqToken.getAccess_token()).getContent();
        Matcher matcher = Pattern.compile("\"openid\":\"(.*?)\"").matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private QQDTO getQQUserInfo(String access_token, String openid) {
        QQ qq = oAuth2.getQq();
        HashMap<String, Object> params = new HashMap<>();
        params.put("access_token", access_token);
        params.put("openid", openid);
        params.put("oauth_consumer_key", qq.getClient_id());
        return Json.fromJson(QQDTO.class, Http.get(qq.getUser_info_uri(), params, 1000).getContent());
    }

}