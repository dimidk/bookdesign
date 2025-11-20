package org.exam.bookdesign.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

//    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
//    private final SecurityContextRepository securityContextRepository;
//    private final AuthenticationProvider authenticationProvider;
    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;



    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {


            http
                    .cors(Customizer.withDefaults())
                    .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/").permitAll()
                .anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 ->
                            //oauth2.jwt(Customizer.withDefaults()))
                            oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)))
//                            oauth2.jwt(jwt -> jwt.jwkSetUri("http://localhost:9090/auth/realms/devrealm/protocol/openid-connect/certs")))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }



}
