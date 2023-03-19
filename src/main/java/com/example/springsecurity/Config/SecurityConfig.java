package com.example.springsecurity.Config;

import com.example.springsecurity.Config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터(Security Config)가 스프링 필터 체인에 등록된다.
@EnableGlobalMethodSecurity(securedEnabled = true) //secured 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;
    //패스워드 암호화
    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    //기본 라우팅 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                 .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                 //.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // 로그인 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행합니다.
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                //로그인이 완료된 후의 후처리가 필요함
                    //1. 코드 받기 (인증)
                    //2. 액세스 토큰(권한)
                    //3. 사용자 프로필 정보를 가져옴
                    //4-1. 가져온 정보를 토대로 회원가입 자동 진행
                    //4-2. 추가 정보 받기
                //액세스 토큰 + 사용자 프로필 정보를 가져옴
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

    }
}
