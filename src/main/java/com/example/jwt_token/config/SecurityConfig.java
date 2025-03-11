package com.example.jwt_token.config;

import com.example.jwt_token.serviceImpl.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Duration;

@Configuration
public class SecurityConfig  {

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("com/users/login","com/users/addUser","com/users/get-refresh-token","/swagger-ui/**","/v3/api-docs/**","/com/users/verify-email").permitAll()
//                        .requestMatchers("/login/oauth2/code/github", "/oauth2/**").permitAll()
//                        .requestMatchers("/login", "/login/oauth2/authorization/github", "/oauth2/**").permitAll()
                        .requestMatchers("/com/users/oauth-success").authenticated()
                        .requestMatchers("/com/users/assignRole").hasRole("Admin")
                        .requestMatchers("/com/users/getAllEmployees").hasAnyRole("Admin","TeamLead","Manager")
                        .requestMatchers("/com/users/getAllTeamLeads").hasAnyRole("Admin","TeamLead")
                        .anyRequest().authenticated())
//                        .oauth2Login(oath -> oath
//
//                                .defaultSuccessUrl("/com/users/oauth-success",true)
//                                .userInfoEndpoint(userinfo -> userinfo.userService(customOAuth2UserService))
//                        )
//               .formLogin(Customizer.withDefaults())
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//                .oauth2Login(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults());
//        return httpSecurity.build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder builder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }

//    @Bean
//    public NimbusJwtDecoder jwtDecoder() {
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
//        jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithClockSkew(Duration.ofMinutes(5)));
//        return jwtDecoder;
//    }
@Bean
public NimbusJwtDecoder jwtDecoder() {
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();

    OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefault();
    OAuth2TokenValidator<Jwt> withClockSkew = new DelegatingOAuth2TokenValidator<>(
            defaultValidator,
            new JwtTimestampValidator(Duration.ofHours(7))
    );

    jwtDecoder.setJwtValidator(withClockSkew);

    return jwtDecoder;
}


}
