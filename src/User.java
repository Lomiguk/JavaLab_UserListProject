public class User {
    private String  FIO;
    private Integer Age;
    private String Phone;
    private Sex Sex;
    private String Address;

    public User(String FIO, Integer age, String phone, Sex sex, String address) {
        this.FIO = FIO;
        Age = age;
        Phone = phone;
        Sex = sex;
        Address = address;
    }

    @Override
    public String toString() {
        return FIO + '\n' +
               Age + '\n'+
               Phone + '\n' +
               Sex + '\n' +
               Address + '\n';
    }
}

