package domain;

public enum Sex {
    FEMALE("Женский"),
    MALE("Мужской"),
    OTHER("Другое");

    private String key;

    Sex(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
