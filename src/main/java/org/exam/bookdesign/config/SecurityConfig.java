package org.exam.bookdesign.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.service.BookUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationProvider authenticationProvider;


   // http://www.noc.ntua.gr/Shibboleth.sso/Login?target=http%3A//www.noc.ntua.gr&amp;forceAuthn=true
    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST",  "DELETE", "OPTIONS"));
    //    configuration.setAllowedHeaders(Arrays.asList( "Content-Type", "Accept"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        //configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin","Origin", "X-Requested-With", "Content-Type", "Accept"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {


        http.exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .cors(c -> c.configurationSource(corsConfiguration()))
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider)
                .securityContext(securityContext -> securityContext.securityContextRepository(securityContextRepository))
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/auth/register").permitAll()
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
                                .anyRequest().authenticated())
                                //.anyRequest().permitAll())
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(form -> form.successHandler(myAuthenticationSuccessHandler))
                .logout(logout -> logout.logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));


        return http.build();

    }

}
