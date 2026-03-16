package account.support;

import com.github.javafaker.Faker;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class TestDataGenerator {

    private static final Faker FAKER = new Faker(new Locale("en"), new Random());

    private static final List<String> STATUSES = List.of(
            "ACTIVE",
            "BLOCKED",
            "PENDING",
            "DISABLED"
    );

    private TestDataGenerator() {
    }

    public static String randomLogin() {
        return normalizeLogin(FAKER.name().username() + FAKER.number().digits(4));
    }

    public static String randomEmail() {
        return FAKER.internet().emailAddress();
    }

    public static String randomPassword() {
        return FAKER.internet().password(8, 16, true, true, true);
    }

    public static String randomName() {
        return FAKER.name().fullName();
    }

    public static String randomLocation() {
        return FAKER.address().city();
    }

    public static String randomStatus() {
        return STATUSES.get(FAKER.random().nextInt(STATUSES.size()));
    }

    public static String unknownLogin() {
        return "unknown_" + FAKER.number().digits(8);
    }

    public static String invalidLogin() {
        return "";
    }

    public static String invalidEmail() {
        return FAKER.lorem().word();
    }

    public static String invalidPassword() {
        return FAKER.lorem().word();
    }

    public static String invalidActivationToken() {
        return "invalid_" + FAKER.regexify("[A-Za-z0-9]{12}");
    }

    public static String invalidAuthToken() {
        return "bad_token_" + FAKER.regexify("[A-Za-z0-9]{10}");
    }

    public static String wrongPassword() {
        return "wrong_" + FAKER.internet().password(8, 16, true, true, true);
    }

    public static String newEmailFrom(String oldEmail) {
        return "new_" + oldEmail;
    }

    public static String newEmail() {
        return "new_" + FAKER.internet().emailAddress();
    }

    public static String emptyPassword() {
        return "";
    }

    public static String newPassword() {
        return "new_" + FAKER.internet().password(8, 16, true, true, true);
    }

    private static String normalizeLogin(String rawLogin) {
        return rawLogin
                .toLowerCase()
                .replaceAll("[^a-z0-9_]", "");
    }
}