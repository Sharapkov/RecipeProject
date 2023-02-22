package com.pasta.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http

                .csrf()
                .disable() // отключает CSRF защиту (защиту от межсайтовой подделки запроса), которая может применяться в приложениях, чтобы предотвратить атаки со стороны злоумышленников.
                .authorizeHttpRequests() //  указывает, что будет использоваться авторизация HTTP запросов.
                .requestMatchers("/api/v1/auth/**")//задает шаблон URL, который необходимо сопоставить для выполнения авторизации.
                .permitAll()//указывает, что доступ к URL, определенному в предыдущем методе, должен быть разрешен всем пользователям без ограничений.
                .anyRequest()
                .authenticated()//означает, что для всех других запросов, которые не были определены в предыдущих методах, требуется авторизация.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//сервер не будет сохранять состояние между запросами, что обеспечивает более безопасный и масштабируемый подход.
                .and()
                .authenticationProvider(authenticationProvider)//добавляет провайдера аутентификации, который будет использоваться для проверки учетных данных пользователя.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);//добавляет фильтр аутентификации, который будет применяться перед фильтром UsernamePasswordAuthenticationFilter. Фильтр jwtAuthFilter будет использоваться для проверки JWT токена, который будет передаваться в заголовке каждого запроса, и в случае успешной проверки будет разрешен доступ к запрашиваемому ресурсу.

        return http.build();
    }

}
