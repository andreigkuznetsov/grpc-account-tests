package account.tests;

import account.ActivateAccountRequest;
import account.ActivateAccountResponse;
import account.RegisterAccountRequest;
import account.RegisterAccountResponse;
import account.base.BaseGrpcTest;
import account.support.TestDataGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivateAccountPositiveTest extends BaseGrpcTest {

    @Test
    void activateAccountShouldActivateRegisteredUser() {
        String login = TestDataGenerator.randomLogin();
        String email = TestDataGenerator.randomEmail();
        String password = TestDataGenerator.randomPassword();

        RegisterAccountResponse registerResponse = blockingStub.registerAccount(
                RegisterAccountRequest.newBuilder()
                        .setLogin(login)
                        .setEmail(email)
                        .setPassword(password)
                        .build()
        );

        assertNotNull(registerResponse);
        assertEquals(login, registerResponse.getLogin());

        String activationToken = mailSteps.waitForActivationToken(login);

        ActivateAccountRequest request = ActivateAccountRequest.newBuilder()
                .setActivationToken(activationToken)
                .build();

        ActivateAccountResponse response = blockingStub.activateAccount(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());
        assertEquals(login, response.getUser().getResource().getLogin());
    }
}