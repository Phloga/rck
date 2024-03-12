package vee.rck.security;

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

import java.security.SecureRandom;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    static final int bcryptStrength = 10;

    @Bean
    @Order(1)
    public SecurityFilterChain unauthorizedSecureFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/login")   // paths are relative to the context path (default "/")
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll()
                ).formLogin(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain unauthorizedFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/*", "/api/**", "/recipe/d/*")
                // /h2-console/** is not controlled by spring
                //.csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()))
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                //        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest()
                        .permitAll())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain defaultAuthorizedFilterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .securityMatcher("/**")
                .csrf( csrf -> csrf.disable())
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(
                        (authorize) -> authorize.anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**", "/assets/**");
    }

    @Bean
    public PasswordEncoder encoder() {
        //Bcrypt generates and includes password salts into the hash, that's the reason for the constructor taking a random number generator
        return new BCryptPasswordEncoder(bcryptStrength, new SecureRandom());
    }

    /*
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_USER \n ROLE_USER > ROLE_ANONYMOUS";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
    */

}
