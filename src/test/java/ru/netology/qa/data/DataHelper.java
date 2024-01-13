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

    public static Date generateInvalidDateMonthGreaterThanTwelfth() {
        int randomYear = LocalDate.now().getYear() + new Random().nextInt(6);
        return new Date("13", String.format("%02d", randomYear % 100));
    }

    public static Date generateInvalidDateOneCharacterMonth() {
        int randomYear = LocalDate.now().getYear() + new Random().nextInt(6);
        return new Date("1", String.format("%02d", randomYear % 100));
    }

    public static Date generateInvalidDateEmptyMonth() {
        int randomYear = LocalDate.now().getYear() + new Random().nextInt(6);
        return new Date("", String.format("%02d", randomYear % 100));
    }

    public static Date generateInvalidDateMonthOfAlphabetical() {
        int randomYear = LocalDate.now().getYear() + new Random().nextInt(6);
        return new Date("AX", String.format("%02d", randomYear % 100));
    }

    public static Date generateInvalidDateMonthOfSpecialCharacter() {
        int randomYear = LocalDate.now().getYear() + new Random().nextInt(6);
        return new Date("%&", String.format("%02d", randomYear % 100));
    }

    public static Date generateInvalidDateMonthOfSpace() {
        int randomYear = LocalDate.now().getYear() + new Random().nextInt(6);
        return new Date(" ", String.format("%02d", randomYear % 100));
    }

    public static Date generateInvalidDateEmptyYear() {
        return new Date(String.format("%02d", new Random().nextInt(12) + 1), "");
    }

    public static Date generateInvalidDateOneCharacterYear() {
        return new Date(String.format("%02d", new Random().nextInt(12) + 1), "1");
    }

    public static Date generateInvalidPastDate() {
        LocalDate currentDate = LocalDate.now().plusMonths(-1);
        int currentYear = currentDate.getYear();
        int pastMonth = currentDate.getMonthValue();
        String year = String.format("%02d", currentYear % 100);
        String month = String.format("%02d", pastMonth);
        return new Date(month, year);
    }

    public static Date generateInvalidFutureDate() {
        LocalDate currentDate = LocalDate.now();
        int randomYear = currentDate.getYear() + 6;
        int randomMonth = currentDate.getMonthValue();
        String year = String.format("%02d", randomYear % 100);
        String month = String.format("%02d", randomMonth);
        return new Date(month, year);
    }

    public static Date generateInvalidDateYearOfAlphabetical() {
        return new Date(String.format("%02d", new Random().nextInt(12) + 1), "AX");
    }

    public static Date generateInvalidDateYearOfSpecialCharacter() {
        return new Date(String.format("%02d", new Random().nextInt(12) + 1), "%&");
    }

    public static Date generateInvalidDateYearOfSpace() {
        return new Date(String.format("%02d", new Random().nextInt(12) + 1), " ");
    }

    public static String generateValidName() {
        String firstName = new Faker(new Locale("en")).name().firstName();
        String lastName = new Faker(new Locale("en")).name().lastName();
        return firstName + " " + lastName;
    }

    public static String generateValidHyphenInAName() {
        String firstName = new Faker(new Locale("en")).name().firstName();
        String lastName = new Faker(new Locale("en")).name().lastName();
        return firstName + "-" + lastName;
    }

    public static String generateRandomSpecialChar() {
        String specialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";
        int randomIndex = new Random().nextInt(specialChars.length());
        return String.valueOf(specialChars.charAt(randomIndex));
    }

    public static String generateRandomNumber() {
        return String.valueOf(new Random().nextInt(10));
    }

    public static String generateValidCvc() {
        return new Faker().regexify("[0-9]{3}");
    }

    public static String generateRandomWord() {
        StringBuilder randomWord = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            char randomLetter = (char) ('a' + new Random().nextInt(26));
            randomWord.append(randomLetter);
        }

        return randomWord.toString();
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
