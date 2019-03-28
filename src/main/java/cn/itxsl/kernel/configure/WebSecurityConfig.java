package cn.itxsl.kernel.configure;

import cn.itxsl.kernel.filter.QQAuthenticationFilter;
import cn.itxsl.kernel.filter.WeiBoAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 13:46
 * @description：安全配置中心
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(
                        "/static/**",
                        "/is/login",
                        "/login/qq",
                        "/login/weibo",
                        "/authorize/qq",
                        "/authorize/weibo",
                        "/",
                        "/index",
                        "/article",
                        "/content",
                        "/message",
                        "/link",
                        "/link/add",
                        "/about",
                        "/love",
                        "/api/link",
                        "/api/message",
                        "/api/article",
                        "/api/comment",
                        "/api/music",
                        "/api/music/*"
                        ).permitAll()
                .antMatchers(
                        "/login",
                        "/api/login"
                )
                .anonymous()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/index")
                .invalidateHttpSession(true)
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index")
                .loginProcessingUrl("/api/login")
                .and()
                .headers().frameOptions().disable()
                .and()
                .addFilterAt(new QQAuthenticationFilter("/authorize/qq"), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new WeiBoAuthenticationFilter("/authorize/weibo"), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
        http
                .sessionManagement()
                .maximumSessions(1)//用户登录限制
                .expiredUrl("/login");
    }
}
