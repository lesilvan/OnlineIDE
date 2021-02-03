package edu.tum.ase.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

//@EnableOAuth2Sso
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .logout()
//                .logoutUrl("/logout")
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID")
//                .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
//                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//                });
////                .and()
////                .csrf()
////                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
////                .and()
////                .authorizeRequests()
////                .antMatchers("/authenticated").permitAll()
////                .antMatchers("/api/**").authenticated();
//    }
//}

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            //configure CORS -- uses a Bean by the name of corsConfigurationSource (see method below)
            //CORS must be configured prior to Spring Security
            .cors()
            .and()
             // CONFIGURE AUTHORIZATION HERE for web tokens
            .authorizeRequests()
                .antMatchers("/**")
                .permitAll()
             // This is needed to let through POST, DELETE, PUT, PATCH etc. http requests
            .and().httpBasic().and().csrf().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(CorsConfiguration.ALL);
        config.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}