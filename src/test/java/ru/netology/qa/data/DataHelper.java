package ru.netology.qa.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;


public class DataHelper {
    public static CardNumber generateValidCardNumberApproved() {
        return new CardNumber("4444 4444 4444 4441", "APPROVED");
    }

    public static CardNumber generateValidCardNumberDeclined() {
        return new CardNumber("4444 4444 4444 4442", "DECLINED");
    }

    public static CardNumber generateEmptyCardNumber() {
        return new CardNumber("", "");
    }

    public static CardNumber generateShortCardNumber() {
        return new CardNumber("1234", "");
    }

    public static CardNumber generateCardNumberOfZeros() {
        return new CardNumber("0000 0000 0000 0000", "");
    }

    public static CardNumber generateAlphabeticalCardNumber() {
        return new CardNumber("Alexander", "");
    }

    public static CardNumber generateCardNumberWithSpecialCharacters() {
        return new CardNumber("^#%&@", "");
    }

    public static CardNumber generateCardNumberWithSpace() {
        return new CardNumber(" ", "");
    }

    public static Date generateValidDate() {
        LocalDate currentDate = LocalDate.now();
        int randomYear = currentDate.getYear() + new Random().nextInt(6);
        int randomMonth = (randomYear == currentDate.getYear()) ?
                currentDate.getMonthValue() + new Random().nextInt(13 - currentDate.getMonthValue()) :
                new Random().nextInt(12) + 1;
        String year = String.format("%02d", randomYear % 100);
        String month = String.format("%02d", randomMonth);
        return new Date(month, year);
    }


    public static String generateValidName() {
        String firstName = new Faker(new Locale("en")).name().firstName();
        String lastName = new Faker(new Locale("en")).name().lastName();
        return firstName + " " + lastName;
    }

    public static String generateValidCvc() {
        return new Faker().regexify("[0-9]{3}");
    }

    @Value
    public static class CardNumber {
        private String cardNumber;
        private String status;
    }

    @Value
    public static class Date {
        private String month;
        private String year;
    }
}
