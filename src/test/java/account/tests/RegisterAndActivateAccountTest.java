package account.tests;

import account.ActivateAccountRequest;
import account.ActivateAccountResponse;
import account.LoginRequest;
import account.LoginResponse;
import account.RegisterAccountRequest;
import account.RegisterAccountResponse;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAndActivateAccountTest extends BaseGrpcTest {

    @Test
    void registerActivateAndLoginShouldSucceed() {
        TestUser user = TestUser.random();

        RegisterAccountRequest registerRequest = RegisterAccountRequest.newBuilder()
                .setLogin(user.login())
                .setEmail(user.email())
                .setPassword(user.password())
                .build();

        RegisterAccountResponse registerResponse = blockingStub.registerAccount(registerRequest);

        assertNotNull(registerResponse);
        assertFalse(registerResponse.getId().isBlank());
        assertEquals(user.login(), registerResponse.getLogin());

        String activationToken = mailSteps.waitForActivationToken(user.login());

        ActivateAccountRequest activateRequest = ActivateAccountRequest.newBuilder()
                .setActivationToken(activationToken)
                .build();

        ActivateAccountResponse activateResponse = blockingStub.activateAccount(activateRequest);

        assertNotNull(activateResponse);
        assertTrue(activateResponse.hasUser());
        assertTrue(activateResponse.getUser().hasResource());
        assertEquals(user.login(), activateResponse.getUser().getResource().getLogin());

        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setLogin(user.login())
                .setPassword(user.password())
                .setRememberMe(true)
                .build();

        LoginResponse loginResponse = blockingStub.login(loginRequest);

        assertNotNull(loginResponse);
        assertFalse(loginResponse.getToken().isBlank());
        assertTrue(loginResponse.hasUser());
        assertTrue(loginResponse.getUser().hasResource());
        assertEquals(user.login(), loginResponse.getUser().getResource().getLogin());
    }
}
