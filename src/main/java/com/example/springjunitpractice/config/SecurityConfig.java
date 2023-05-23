package com.example.springjunitpractice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.springjunitpractice.config.common.CustomAccessDeniedHandler;
import com.example.springjunitpractice.config.common.CustomAuthenticationEntryPoint;
import com.example.springjunitpractice.config.jwt.JwtAuthenticationFilter;
import com.example.springjunitpractice.config.jwt.JwtAuthorizationFilter;
import com.example.springjunitpractice.domain.user.UserEnum;

@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    // @Configuration 클래스 내에 선언된 @Bean 메소드는 여러 번 호출돼도 싱글톤으로 관리되는 것을 보장함
    // 비밀번호를 암호화하는 BCryptPasswordEncoder 빈 생성
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        log.debug("DEBUG: BCryptPasswordEncoder 빈 등록");
        return new BCryptPasswordEncoder();
    }

    // JwtAuthenticationFilter, JwtAuthorizationFilter 필터에서 사용할
    // AuthenticationManager를 빈으로 등록
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable() // iframe 허용 안함
                .and()
                .csrf().disable() // CSRF 설정되어 있으면 Postman 작동 안함
                .cors().configurationSource(configurationSource()); // CORS 설정

        // JWT 기본 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용하지 안함
                .and()
                .formLogin().disable() // Form 로그인 인증 방식 사용 안함
                .httpBasic().disable(); // Bearer 인증 방식 사용 (토큰 인증)

        // JWT 필터 등록
        http.addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager()));

        http.authorizeRequests()
                .antMatchers("/api/s/**").authenticated()
                .antMatchers("/api/admin/**").hasRole(UserEnum.ADMIN.name())
                .anyRequest().permitAll();

        // 시큐리티 필터에서 발생한 Exception 처리
        // (참고) 필터 통과 후 컨트롤러에서 발생한 Exception은 CustomExceptionHandler 클래스가 처리함
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 실패 시 처리
                .accessDeniedHandler(new CustomAccessDeniedHandler()); // 인가 실패 시 처리

        return http.build();
    }

    // 자바스크립트 CORS(Cross-Origin Resource Sharing) 설정 빈 등록
    @Bean
    CorsConfigurationSource configurationSource() {
        log.debug("DEBUG: CORS 설정 빈 등록");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 요청에 포함된 쿠키 및 인증 정보를 내 서버에서 사용할 수 있도록 허용
        config.addAllowedOriginPattern("*"); // 내 서버로 요청을 보낼 수 있는 출처(Origin) 설정
        config.addAllowedHeader("*"); // 내 서버로 요청을 보낼 수 있는 HTTP 헤더 설정
        config.addAllowedMethod("*"); // 내 서버로 요청을 보낼 수 있는 HTTP 메서드 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 해당 URL 패턴에 CORS 설정 등록
        return source;
    }
}
