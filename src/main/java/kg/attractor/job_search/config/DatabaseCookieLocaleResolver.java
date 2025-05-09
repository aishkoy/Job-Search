package kg.attractor.job_search.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.attractor.job_search.service.UserService;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

public class DatabaseCookieLocaleResolver implements LocaleContextResolver {
    private final CookieLocaleResolver cookieLocaleResolver;
    private final UserService userService;

    public DatabaseCookieLocaleResolver(UserService userService) {
        this.userService = userService;
        this.cookieLocaleResolver = new CookieLocaleResolver("language");
        this.cookieLocaleResolver.setDefaultLocale(new Locale("ru"));
        this.cookieLocaleResolver.setCookieMaxAge(365 * 24 * 60 * 60);
    }

    @Override
    public LocaleContext resolveLocaleContext(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String)) {
            try {
                String email = authentication.getName();
                String preferredLanguage = userService.getUserPreferredLanguage(email);
                if (preferredLanguage != null && !preferredLanguage.isEmpty()) {
                    return new SimpleLocaleContext(new Locale(preferredLanguage));
                }
            } catch (Exception ignored) {
            }
        }

        return cookieLocaleResolver.resolveLocaleContext(request);
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
        cookieLocaleResolver.setLocaleContext(request, response, localeContext);
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return resolveLocaleContext(request).getLocale();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        setLocaleContext(request, response, new SimpleLocaleContext(locale));
    }
}