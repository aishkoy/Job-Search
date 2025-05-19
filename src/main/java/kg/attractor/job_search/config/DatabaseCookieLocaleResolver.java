package kg.attractor.job_search.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

@Slf4j
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
        LocaleContext cookieLocaleContext = cookieLocaleResolver.resolveLocaleContext(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !("anonymousUser".equals(authentication.getPrincipal()))) {
            try {
                String email = authentication.getName();
                String preferredLanguage = userService.getUserPreferredLanguage(email);
                if (preferredLanguage != null && !preferredLanguage.isEmpty()) {
                    return new SimpleLocaleContext(new Locale(preferredLanguage));
                }
            } catch (Exception e) {
                log.warn("Failed to retrieve user's preferred language: " + e.getMessage());
            }
        }

        return cookieLocaleContext;
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
        cookieLocaleResolver.setLocaleContext(request, response, localeContext);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !("anonymousUser".equals(authentication.getPrincipal()))) {
            try {
                String email = authentication.getName();
                Locale locale = localeContext.getLocale();
                if (locale != null) {
                    userService.updateUserLanguage(email, locale.getLanguage());
                }
            } catch (Exception e) {
                log.warn("Failed to update user's preferred language: " + e.getMessage());
            }
        }
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        LocaleContext localeContext = resolveLocaleContext(request);
        return localeContext != null ? localeContext.getLocale() : new Locale("ru");
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        setLocaleContext(request, response, new SimpleLocaleContext(locale));
    }
}