package domain;

public class User {
    private final String  FIO;
    private final Integer Age;
    private final String Phone;
    private final Sex Sex;
    private final String Address;

    public User(String FIO, Integer age, String phone, Sex sex, String address) {
        this.FIO = FIO;
        Age = age;
        Phone = phone;
        Sex = sex;
        Address = address;
    }

    @Override
    public String toString() {
        String UserString = new StringBuilder()
                .append(FIO).append("\r\n")
                .append(Age).append("\r\n")
                .append(Phone).append("\r\n")
                .append(Sex).append("\r\n")
                .append(Address).append("\r\n")
                .toString();
        return UserString;
    }

    public String getFIO() {
        return FIO;
    }

    public Integer getAge() {
        return Age;
    }

    public String getPhone() {
        return Phone;
    }

    public Sex getSex() {
        return Sex;
    }

    public String getAddress() {
        return Address;
    }
}

