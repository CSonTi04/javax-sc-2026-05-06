package employees;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        registry ->
                                registry.requestMatchers("/")
                                        .permitAll()
                                        .requestMatchers("/create-employee")
                                        .authenticated()
                                        .anyRequest()
                                        .hasRole("employee_admin")
                )
                .oauth2Login(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/"))
        ;

        return http.build();
    }
}
