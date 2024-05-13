package com.bamboo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/members/login")
                                .defaultSuccessUrl("/")
                                .usernameParameter("name")
                                .failureUrl("/members/login/error")
                )
                .logout((logoutConfig) ->
                        logoutConfig
                                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                                .logoutSuccessUrl("/")
                )
                .authorizeRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                                .requestMatchers("/", "members/**", "/item/**", "/images/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint("/members/login"))
        );

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 정적 리소스 ignore
/* @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }*/


}
