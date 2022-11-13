package domain;

public enum Category {
    NAME("Имя"),
    AGE("Возраст"),
    PHONE("Телефон"),
    SEX("Пол"),
    ADDRESS("Адресс");

    private final String key;

    Category(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
