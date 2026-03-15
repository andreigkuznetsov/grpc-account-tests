package account.steps;

import account.AccountServiceGrpc;
import account.ActivateAccountRequest;
import account.ActivateAccountResponse;
import account.LoginRequest;
import account.LoginResponse;
import account.RegisterAccountRequest;
import account.RegisterAccountResponse;
import account.model.TestUser;
import account.support.TestDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class UserFlowSteps {

    private static final Logger log = LoggerFactory.getLogger(UserFlowSteps.class);

    private final AccountServiceGrpc.AccountServiceBlockingStub blockingStub;
    private final MailSteps mailSteps;

    public UserFlowSteps(AccountServiceGrpc.AccountServiceBlockingStub blockingStub, MailSteps mailSteps) {
        this.blockingStub = blockingStub;
        this.mailSteps = mailSteps;
    }

    public TestUser registerActivateAndLogin() {
        String login = TestDataGenerator.randomLogin();
        String email = TestDataGenerator.randomEmail();
        String password = TestDataGenerator.randomPassword();

        log.info("Starting user flow: register -> activate -> login");
        log.info("Generated test user: login={}, email={}", login, email);

        RegisterAccountRequest registerRequest = RegisterAccountRequest.newBuilder()
                .setLogin(login)
                .setEmail(email)
                .setPassword(password)
                .build();

        log.info("Calling RegisterAccount for login={}", login);
        RegisterAccountResponse registerResponse = blockingStub.registerAccount(registerRequest);

        assertNotNull(registerResponse);
        assertFalse(registerResponse.getId().isBlank());
        assertEquals(login, registerResponse.getLogin());

        log.info("RegisterAccount success: id={}, login={}", registerResponse.getId(), registerResponse.getLogin());

        log.info("Waiting for activation token for login={}", login);
        String activationToken = mailSteps.waitForActivationToken(login);
        log.info("Activation token received for login={}", login);

        ActivateAccountRequest activateRequest = ActivateAccountRequest.newBuilder()
                .setActivationToken(activationToken)
                .build();

        log.info("Calling ActivateAccount for login={}", login);
        ActivateAccountResponse activateResponse = blockingStub.activateAccount(activateRequest);

        assertNotNull(activateResponse);
        assertTrue(activateResponse.hasUser());
        assertTrue(activateResponse.getUser().hasResource());
        assertEquals(login, activateResponse.getUser().getResource().getLogin());

        log.info("ActivateAccount success for login={}", login);

        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .setRememberMe(true)
                .build();

        log.info("Calling Login for login={}", login);
        LoginResponse loginResponse = blockingStub.login(loginRequest);

        assertNotNull(loginResponse);
        assertFalse(loginResponse.getToken().isBlank());
        assertTrue(loginResponse.hasUser());
        assertTrue(loginResponse.getUser().hasResource());
        assertEquals(login, loginResponse.getUser().getResource().getLogin());

        log.info("Login success for login={}, token received", login);

        return new TestUser(login, email, password, loginResponse.getToken());
    }
}