package kg.attractor.job_search.enums;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.regex.Pattern;

@Getter
public enum ContactType {
    EMAIL(1L, "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", "contact.validation.email"),
    PHONE(2L, "^\\+?[0-9]{10,15}$", "contact.validation.phone"),
    TELEGRAM(3L, "^@[A-Za-z0-9_]{5,32}$", "contact.validation.telegram"),
    FACEBOOK(4L, "^(?:(?:https?:\\/\\/)?(?:www\\.)?facebook\\.com\\/)?[A-Za-z0-9.]{5,50}(?:\\/.*)?$",
            "contact.validation.facebook"),
    LINKEDIN(5L, "^(?:(?:https?:\\/\\/)?(?:www\\.)?linkedin\\.com\\/(?:in|profile)\\/)?[A-Za-z0-9-]{5,100}(?:\\/.*)?$",
            "contact.validation.linkedin");

    private final Long id;
    private final Pattern pattern;
    private final String messageKey;

    ContactType(Long id, String regex, String messageKey) {
        this.id = id;
        this.pattern = Pattern.compile(regex);
        this.messageKey = messageKey;
    }

    public String getLocalizedErrorMessage(MessageSource messageSource) {
        return messageSource.getMessage(
                messageKey,
                null,
                LocaleContextHolder.getLocale()
        );
    }

    public boolean validate(String value) {
        return pattern.matcher(value).matches();
    }

    public static ContactType fromId(Long id) {
        if (id == null) {
            return null;
        }

        for (ContactType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return null;
    }
}