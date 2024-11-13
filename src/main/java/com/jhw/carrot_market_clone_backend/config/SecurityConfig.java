package com.jhw.carrot_market_clone_backend.config;

import com.jhw.carrot_market_clone_backend.controller.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    // SecurityFilterChain : 컨테이너에서 filter chain을 구성할 때,
    // 해당 클래스 또한 filter chain 구조에 포함되도록 한다.
    // filter chain 내에는 httpSecurity에 설정한 값들을 기반으로 생성된다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // session에 대한 공격을 방지하는 것이지만, jwt 검증은 이와 결이 다르므로 disable
                .csrf(AbstractHttpConfigurer::disable)
                // JWT 인증 방식은 stateless 한 방식이므로, 이에 맞게 세션 정책을 설정해준다.
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // UsernamePasswordAuthenticationFilter 필터 이전에 jwtAuthenticationFilter 필터가 수행되도록 설정
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}