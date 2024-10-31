package com.pasta.config;

import com.pasta.security.JwtConfigurer;
import com.pasta.security.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    @Value("${jwt.header.string}")
    public String jwtHeaderString;
    @Value("${jwt.token.prefix}")
    public String jwtTokenPrefix;
    private final UserDetailsService userDetailsService;
    private final CorsFilter corsFilter;
    private final TokenProvider tokenProvider;

    public WebSecurityConfig(CorsFilter corsFilter,
                             TokenProvider tokenProvider,
                             UserDetailsService userDetailsService) {
        this.corsFilter = corsFilter;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**")
                .antMatchers("/test/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(securityConfigurerAdapter());

        http
            .authorizeRequests()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api-docs/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user/registration").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/user/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/demo-controller").permitAll()
                .antMatchers("/url").permitAll()
                .antMatchers("/url/**").permitAll()
                .antMatchers("/lk/**").permitAll()
                .antMatchers("/chat/**").permitAll()
                .antMatchers("/*.html").permitAll()
                .antMatchers("/*.js").permitAll()
                .anyRequest().authenticated();
        return http.build();
    }

    private JwtConfigurer securityConfigurerAdapter() {
        return new JwtConfigurer(tokenProvider, userDetailsService, jwtHeaderString, jwtTokenPrefix);
    }

}
