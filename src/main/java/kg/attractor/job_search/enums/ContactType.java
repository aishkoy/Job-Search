package kg.attractor.job_search.enums;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public enum ContactType {
    EMAIL(1L, "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", "Email должен быть в формате example@domain.com"),
    PHONE(2L, "^\\+?[0-9]{10,15}$", "Телефон должен содержать от 10 до 15 цифр, может начинаться с +"),
    TELEGRAM(3L, "^@[A-Za-z0-9_]{5,32}$", "Telegram должен начинаться с @ и содержать от 5 до 32 символов"),
    FACEBOOK(4L, "^(?:(?:https?:\\/\\/)?(?:www\\.)?facebook\\.com\\/)?[A-Za-z0-9.]{5,50}(?:\\/.*)?$",
            "Facebook должен содержать имя пользователя или полную ссылку на профиль (например, facebook.com/username)"),
    LINKEDIN(5L, "^(?:(?:https?:\\/\\/)?(?:www\\.)?linkedin\\.com\\/(?:in|profile)\\/)?[A-Za-z0-9-]{5,100}(?:\\/.*)?$",
            "LinkedIn должен содержать имя пользователя или полную ссылку на профиль (например, linkedin.com/in/username)");

    private final Long id;
    private final Pattern pattern;
    private final String errorMessage;

    ContactType(Long id, String regex, String errorMessage) {
        this.id = id;
        this.pattern = Pattern.compile(regex);
        this.errorMessage = errorMessage;
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