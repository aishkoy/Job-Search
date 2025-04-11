package kg.attractor.job_search.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DataSource dataSource;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        String fetchUser = "select EMAIL, PASSWORD, ENABLED from USERS where EMAIL = ?;";

        String fetchRole = """
                select u.EMAIL as EMAIL, concat('ROLE_', r.ROLE) as ROLE
                from USERS u, ROLES r
                where u.EMAIL = ?
                and u.ROLE_ID = r.ID;
                """;

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(fetchUser)
                .authoritiesByUsernameQuery(fetchRole);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/profile")
                        .failureUrl("/auth/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(HttpMethod.POST, "/resumes/**", "/responses/**").hasRole("APPLICANT")
                                .requestMatchers(HttpMethod.PUT, "/resumes/**").hasRole("APPLICANT")
                                .requestMatchers(HttpMethod.DELETE, "/resumes/**").hasRole("APPLICANT")
                                .requestMatchers(HttpMethod.GET, "/vacancies/applied/**").hasRole("APPLICANT")
                                .requestMatchers(HttpMethod.GET, "/resumes/{id}").hasRole("APPLICANT")

                                .requestMatchers(HttpMethod.GET, "/resumes/**").hasRole("EMPLOYER")
                                .requestMatchers(HttpMethod.POST, "/vacancies/**").hasRole("EMPLOYER")
                                .requestMatchers(HttpMethod.PUT, "/vacancies/**").hasRole("EMPLOYER")
                                .requestMatchers(HttpMethod.DELETE, "/vacancies/**").hasRole("EMPLOYER")
                                .requestMatchers(HttpMethod.GET, "/users/responded/**", "/users/applicants/**").hasRole("EMPLOYER")

                                .requestMatchers(HttpMethod.DELETE, "/users/{id}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/users/{id}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/users/{id}/avatar").authenticated()

                                .requestMatchers(HttpMethod.GET, "/users",
                                        "/users/by-name",
                                        "/users/by-phone",
                                        "/users/by-email",
                                        "/users/exists").hasRole("ADMIN")
                                .anyRequest().permitAll()
                );
        return http.build();
    }

}
