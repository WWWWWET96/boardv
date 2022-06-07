package com.example.boardv.config.auth;

import com.example.boardv.config.auth.service.General.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class GeneralSecurityCofig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
//    * Spring security에서 모든 인증처리는 AuthenticationManager를 통해 이루어지는데,
//    * AuthenticationManager를 생성하기 위해서 AuthenticationManagerBuilder를 사용해 생성
    @Override
    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
        auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**","/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/auth/**", "/posts/read/**", "/posts/search/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()//login 경로로 접근하면, Spring Security에서 제공하는 로그인 Form을 사용가능
                .loginPage("/auth/login")//기본으로 제공되는 form말고, 커스텀 로그인 폼을 사용하기위해 사용하는 메소드
                .loginProcessingUrl("/loginProc")//security에서 해당 주소로 오는 요청을 낚아채서 수행
                .defaultSuccessUrl("/")//로그인 성공 시 이동되는 페이지
                .and()
                .logout()//"/logout"에 접근하면 HTTP세션을 제거해줌
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);//HTTP세션을 초기화하는 작업
    }
  /*




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder());
    }

    //인증을 무시할 경로를 설정해줌. 밑 주소들은 무조건 접근 가능해야하기 때문에 인증을 무시하게 해 줌
    @Override
    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/css./**", "/js/**", "/img/**");
    }

    //HttpSecurity를 통해 HTTP요청에 대한 보안을 구성할 수 있음

*/
}
