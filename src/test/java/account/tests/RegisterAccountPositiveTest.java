package account.tests;

import account.RegisterAccountRequest;
import account.RegisterAccountResponse;
import account.base.BaseGrpcTest;
import account.support.TestDataGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAccountPositiveTest extends BaseGrpcTest {

    @Test
    void registerAccountShouldCreateNewUser() {
        String login = TestDataGenerator.randomLogin();
        String email = TestDataGenerator.randomEmail();
        String password = TestDataGenerator.randomPassword();

        RegisterAccountRequest request = RegisterAccountRequest.newBuilder()
                .setLogin(login)
                .setEmail(email)
                .setPassword(password)
                .build();

        RegisterAccountResponse response = blockingStub.registerAccount(request);

        assertNotNull(response);
        assertFalse(response.getId().isBlank());
        assertEquals(login, response.getLogin());
    }
}
