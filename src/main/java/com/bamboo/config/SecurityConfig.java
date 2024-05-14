package com.bamboo.config;

import com.bamboo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            HttpSecurity http) throws Exception {

        return null;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/members/login")
                                .defaultSuccessUrl("/")
                                .usernameParameter("email")
                                .failureUrl("/members/login/error")

                )
                .logout((logoutConfig) ->
                        logoutConfig
                                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                                .logoutSuccessUrl("/")
                )
                .csrf((csrf) ->
                        csrf
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                /*.authorizeRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                                .requestMatchers("/", "members/**", "/item/**", "/images/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )

                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint("/members/login"))
                )*/
        ;
        return http.build();
    }

    // 정적 리소스 ignore
   /* @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }*/

}
