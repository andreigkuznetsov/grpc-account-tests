package account.model;

import account.LoginRequest;
import account.RegisterAccountRequest;
import account.support.TestDataGenerator;

public record TestUser(
        String login,
        String email,
        String password,
        String token
) {

    public static TestUser random() {
        return new TestUser(
                TestDataGenerator.randomLogin(),
                TestDataGenerator.randomEmail(),
                TestDataGenerator.randomPassword(),
                null
        );
    }

    public static TestUser withLogin(String login) {
        return new TestUser(
                login,
                TestDataGenerator.randomEmail(),
                TestDataGenerator.randomPassword(),
                null
        );
    }

    public static TestUser withEmail(String email) {
        return new TestUser(
                TestDataGenerator.randomLogin(),
                email,
                TestDataGenerator.randomPassword(),
                null
        );
    }

    public static TestUser withPassword(String password) {
        return new TestUser(
                TestDataGenerator.randomLogin(),
                TestDataGenerator.randomEmail(),
                password,
                null
        );
    }

    public static TestUser invalidLoginUser() {
        return new TestUser(
                TestDataGenerator.invalidLogin(),
                TestDataGenerator.randomEmail(),
                TestDataGenerator.randomPassword(),
                null
        );
    }

    public static TestUser invalidEmailUser() {
        return new TestUser(
                TestDataGenerator.randomLogin(),
                TestDataGenerator.invalidEmail(),
                TestDataGenerator.randomPassword(),
                null
        );
    }

    public static TestUser invalidPasswordUser() {
        return new TestUser(
                TestDataGenerator.randomLogin(),
                TestDataGenerator.randomEmail(),
                TestDataGenerator.invalidPassword(),
                null
        );
    }

    public RegisterAccountRequest toRegisterRequest() {
        return RegisterAccountRequest.newBuilder()
                .setLogin(login)
                .setEmail(email)
                .setPassword(password)
                .build();
    }

    public LoginRequest toLoginRequest(boolean rememberMe) {
        return LoginRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .setRememberMe(rememberMe)
                .build();
    }

    public LoginRequest toLoginRequestWithPassword(String password, boolean rememberMe) {
        return LoginRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .setRememberMe(rememberMe)
                .build();
    }

    public TestUser withToken(String token) {
        return new TestUser(login, email, password, token);
    }

    public boolean isAuthorized() {
        return token != null && !token.isBlank();
    }
}