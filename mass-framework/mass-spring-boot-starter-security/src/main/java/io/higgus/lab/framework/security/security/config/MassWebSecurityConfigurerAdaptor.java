package io.higgus.lab.framework.security.security.config;


import com.google.common.collect.Multimap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MassWebSecurityConfigurerAdaptor {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().permitAll()
                );
//                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .headers(c ->
//                        c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

//        Multimap<HttpMethod, String> permitAllUrls = getPermitAllUrlsFromAnnotations();



        return http.build();
    }



//    private Multimap<HttpMethod, String> getPermitAllUrlsFromAnnotations() {
//
//    }
}
