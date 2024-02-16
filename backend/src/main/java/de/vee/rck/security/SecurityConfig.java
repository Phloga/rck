package de.vee.rck.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.SecureRandom;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    static final int bcryptStrength = 10;

    @Bean
    @Order(1)
    public SecurityFilterChain csrfEnabledFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/*")
                // /h2-console/** is not controlled by spring, which makes the security matcher
                //.csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()))
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest()
                        .permitAll())
                .formLogin(Customizer.withDefaults()).httpBasic(withDefaults());
        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain csrfDisabledFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/api/**", "/assets/**")   // paths are relative to the context path (default "/")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll()
                );
        return http.build();
    }


    @Bean
    @Order(3)
    public SecurityFilterChain secureFilterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .securityMatcher("/**")
                .authorizeHttpRequests(
                        (authorize) -> authorize.anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults()).httpBasic(withDefaults());;
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder encoder() {
        //Bcrypt generates and includes password salts into the hash, that's the reason for the constructor taking a random number generator
        return new BCryptPasswordEncoder(bcryptStrength, new SecureRandom());
    }
}
