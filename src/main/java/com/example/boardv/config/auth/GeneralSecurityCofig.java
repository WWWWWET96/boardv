package com.example.boardv.config.auth;

import com.example.boardv.config.auth.service.General.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GeneralSecurityCofig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthFailureHandler customFailureHandler; //로그인 실패 핸들러 의존성 주입

    @Bean
    public BCryptPasswordEncoder encoder(){ //시큐리티가 로그인 과정에서 password를 가로챌 때 어떤 해쉬로 암호화했는지 확인
        return new BCryptPasswordEncoder();
    }
//    * Spring security에서 모든 인증처리는 AuthenticationManager를 통해 이루어지는데,
//    * AuthenticationManager를 생성하기 위해서 AuthenticationManagerBuilder를 사용해 생성
    @Override
    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
        auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder());


/* //auth객체에 CustomUserDetailsService를 등록하여 사용자 조회를 UserSecurityService가
            담당하도록 설정
            이때, 비밀번호 검증에 사용할 passwordEncoder도 함께 등록*/
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**","/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/resources/**","/posts/**", "/posts/save","/error", "/auth/**", "/api/v1/posts/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()//login 경로로 접근하면, Spring Security에서 제공하는 로그인 Form을 사용가능
                .loginPage("/auth/login")//기본으로 제공되는 form말고, 커스텀 로그인 폼을 사용하기위해 사용하는 메소드
                .loginProcessingUrl("/auth/loginProc")//security에서 해당 주소로 오는 요청을 낚아채서 수행
                .failureHandler(customFailureHandler)//로그인 실패 핸들러
                .defaultSuccessUrl("/")//로그인 성공 시 이동되는 페이지
                .and()
                .logout()//"/logout"에 접근하면 HTTP세션을 제거해줌
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .logout()
                .invalidateHttpSession(true).deleteCookies("JSESSIONID");//HTTP세션을 초기화하는 작업
    }

}
