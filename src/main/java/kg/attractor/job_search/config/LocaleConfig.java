package kg.attractor.job_search.config;

import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@RequiredArgsConstructor
public class LocaleConfig implements WebMvcConfigurer {
    
    private final UserService userService;
    
    @Bean
    public LocaleResolver localeResolver() {
        return new DatabaseCookieLocaleResolver(userService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var loc = new LocaleChangeInterceptor();
        loc.setParamName("lang");
        return loc;
    }
}