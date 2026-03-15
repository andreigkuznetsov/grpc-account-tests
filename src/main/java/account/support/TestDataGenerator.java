package account.support;

import java.util.UUID;

public final class TestDataGenerator {

    private TestDataGenerator() {
    }

    public static String randomLogin() {
        return "autotest_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    public static String randomEmail() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12) + "@mail.test";
    }

    public static String randomPassword() {
        return "Qwerty123!" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }

    public static String randomStatus() {
        return "status_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public static String randomName() {
        return "name_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public static String randomLocation() {
        return "location_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}