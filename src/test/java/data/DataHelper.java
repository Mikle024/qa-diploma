package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class DataHelper {
    public static CardNumber setValidCardNumberApproved() {
        return new CardNumber("4444 4444 4444 4441", "APPROVED");
    }

    public static CardNumber setValidCardNumberDeclined() {
        return new CardNumber("4444 4444 4444 44442", "DECLINED");
    }

    public static String generateMonth(int shift) {
        return LocalDate.now().plusMonths(shift).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String generateYear(int shift) {
        return LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateName(String local) {
        String firstName = new Faker(new Locale(local)).name().firstName();
        String lastName = new Faker(new Locale(local)).name().lastName();
        return firstName + " " + lastName;
    }

    public static String generateCvc() {
        var faker = new Faker();
        return faker.regexify("[0-9]{3}");
    }

    @Value
    public static class CardNumber {
        private String cardNumber;
        private String status;
    }
}
