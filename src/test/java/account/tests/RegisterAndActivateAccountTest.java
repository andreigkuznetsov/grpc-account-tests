package account.tests;

import account.ActivateAccountRequest;
import account.ActivateAccountResponse;
import account.LoginRequest;
import account.LoginResponse;
import account.RegisterAccountRequest;
import account.RegisterAccountResponse;
import account.base.BaseGrpcTest;
import account.support.TestDataGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAndActivateAccountTest extends BaseGrpcTest {

    @Test
    void registerActivateAndLoginShouldSucceed() {
        String login = TestDataGenerator.randomLogin();
        String email = TestDataGenerator.randomEmail();
        String password = TestDataGenerator.randomPassword();

        RegisterAccountRequest registerRequest = RegisterAccountRequest.newBuilder()
                .setLogin(login)
                .setEmail(email)
                .setPassword(password)
                .build();

        RegisterAccountResponse registerResponse = blockingStub.registerAccount(registerRequest);

        assertNotNull(registerResponse);
        assertFalse(registerResponse.getId().isBlank());
        assertEquals(login, registerResponse.getLogin());

        String activationToken = mailSteps.waitForActivationToken(login);

        ActivateAccountRequest activateRequest = ActivateAccountRequest.newBuilder()
                .setActivationToken(activationToken)
                .build();

        ActivateAccountResponse activateResponse = blockingStub.activateAccount(activateRequest);

        assertNotNull(activateResponse);
        assertTrue(activateResponse.hasUser());
        assertTrue(activateResponse.getUser().hasResource());
        assertEquals(login, activateResponse.getUser().getResource().getLogin());

        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .setRememberMe(true)
                .build();

        LoginResponse loginResponse = blockingStub.login(loginRequest);

        assertNotNull(loginResponse);
        assertFalse(loginResponse.getToken().isBlank());
        assertTrue(loginResponse.hasUser());
        assertTrue(loginResponse.getUser().hasResource());
        assertEquals(login, loginResponse.getUser().getResource().getLogin());
    }
}
