package account.steps;

import account.AccountServiceGrpc;
import account.ActivateAccountRequest;
import account.ActivateAccountResponse;
import account.LoginRequest;
import account.LoginResponse;
import account.RegisterAccountRequest;
import account.RegisterAccountResponse;
import account.model.TestUser;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UserFlowSteps {

    private final AccountServiceGrpc.AccountServiceBlockingStub blockingStub;
    private final MailSteps mailSteps;

    public UserFlowSteps(AccountServiceGrpc.AccountServiceBlockingStub blockingStub, MailSteps mailSteps) {
        this.blockingStub = blockingStub;
        this.mailSteps = mailSteps;
    }

    public TestUser registerActivateAndLogin() {
        TestUser user = TestUser.random();

        log.info("Starting user flow: register -> activate -> login");
        log.info("Generated test user: login={}, email={}", user.login(), user.email());

        RegisterAccountRequest registerRequest = RegisterAccountRequest.newBuilder()
                .setLogin(user.login())
                .setEmail(user.email())
                .setPassword(user.password())
                .build();

        log.info("Calling RegisterAccount for login={}", user.login());
        RegisterAccountResponse registerResponse = blockingStub.registerAccount(registerRequest);

        assertNotNull(registerResponse);
        assertFalse(registerResponse.getId().isBlank());
        assertEquals(user.login(), registerResponse.getLogin());

        log.info("RegisterAccount success: id={}, login={}", registerResponse.getId(), registerResponse.getLogin());

        log.info("Waiting for activation token for login={}", user.login());
        String activationToken = mailSteps.waitForActivationToken(user.login());
        log.info("Activation token received for login={}", user.login());

        ActivateAccountRequest activateRequest = ActivateAccountRequest.newBuilder()
                .setActivationToken(activationToken)
                .build();

        log.info("Calling ActivateAccount for login={}", user.login());
        ActivateAccountResponse activateResponse = blockingStub.activateAccount(activateRequest);

        assertNotNull(activateResponse);
        assertTrue(activateResponse.hasUser());
        assertTrue(activateResponse.getUser().hasResource());
        assertEquals(user.login(), activateResponse.getUser().getResource().getLogin());

        log.info("ActivateAccount success for login={}", user.login());

        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setLogin(user.login())
                .setPassword(user.password())
                .setRememberMe(true)
                .build();

        log.info("Calling Login for login={}", user.login());
        LoginResponse loginResponse = blockingStub.login(loginRequest);

        assertNotNull(loginResponse);
        assertFalse(loginResponse.getToken().isBlank());
        assertTrue(loginResponse.hasUser());
        assertTrue(loginResponse.getUser().hasResource());
        assertEquals(user.login(), loginResponse.getUser().getResource().getLogin());

        log.info("Login success for login={}, token received", user.login());

        return user.withToken(loginResponse.getToken());
    }
}