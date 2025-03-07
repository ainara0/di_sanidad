package main.ui;

import main.connection.Patient;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataCheck {
    public static boolean[] checkPatientData(Patient patient) {
        boolean[] correctData = new boolean[12];

        String dni = patient.getDni();
        String name = patient.getName();
        String surname1 = patient.getSurname1();
        String surname2 = patient.getSurname2();
        LocalDate birthDate = patient.getBirthDate();
        String email = patient.getEmail();
        String telephone = patient.getTelephone();
        String city = patient.getCity();
        String postalCode = patient.getPostalCode();
        String street = patient.getStreet();
        String number = patient.getNumber();
        String password = patient.getPassword();

        correctData[0] = validateDni(dni);
        correctData[1] = validateVarchar50(name);
        correctData[2] = validateVarchar50(surname1);
        correctData[3] = validateVarchar50(surname2);
        correctData[4] = validateBirthDate(birthDate);
        correctData[5] = validateEmail(email);
        correctData[6] = validateTelephone(telephone);
        correctData[7] = validateVarchar50(city);
        correctData[8] = validatePostalCode(postalCode);
        correctData[9] = validateVarchar50(street);
        correctData[10] = validateNumber(number);
        correctData[11] = validatePassword(password);
        return correctData;
    }

    public static boolean validatePassword(String password) {
        return password != null && password.length() > 7 && password.length() < 16;
    }

    public static boolean validateNumber(String number) {
        System.out.println(number);
        return (number == null || number.length() < 50);
    }
    public static boolean validatePostalCode(String postalCode) {
        return postalCode != null && isPostalCodeCorrect(postalCode);
    }

    public static boolean validateTelephone(String telephone) {
        return telephone != null && isTelephoneCorrect(telephone);
    }

    public static boolean validateEmail(String email) {
        return email != null && (email.length() > 6) && (email.length() < 100) && isEmailCorrect(email);
    }

    public static boolean validateBirthDate(LocalDate birthDate) {
        return birthDate != null && !birthDate.isAfter(LocalDate.now());
    }

    public static boolean validateVarchar50(String string) {
        return string != null && (string.length() > 2) && (string.length() < 50);
    }

    public static boolean validateDni(String dni) {
        return (dni != null && isDNICorrect(dni));
    }

    public static boolean isDNICorrect(String input) {
        final Pattern pattern = Pattern.compile("[0-9]{8}[A-Z]", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public static boolean isEmailCorrect(String input) {
        final Pattern pattern = Pattern.compile("[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public static boolean isTelephoneCorrect(String input) {
        final Pattern pattern = Pattern.compile("[0-9]{9}", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public static boolean isPostalCodeCorrect(String input) {
        final Pattern pattern = Pattern.compile("[0-9]{5}", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
