package com.example.StudentProfile.config;

import com.example.StudentProfile.config.jwt.JwtAuthenticationEntryPoint;
import com.example.StudentProfile.config.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
//                .antMatchers("/api/v1/user/*").hasAuthority("student")
//                .antMatchers("/api/v1/admin/users/*").hasAuthority("admin")
//                .antMatchers("/api/v1/users/*").permitAll() //.hasAuthority("student")
                .antMatchers("/api/v1/admin/users/*", "/api/v1/admin/backup","/api/v1/admin/statistic").hasAuthority("admin")
                .antMatchers("/api/v1/signup", "/api/v1/signin").permitAll()
                .antMatchers("/api/v1/users/*", "/api/v1/users").permitAll() //.hasAuthority("student")

                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement()
                .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
