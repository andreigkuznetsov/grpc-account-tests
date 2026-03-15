package account.tests;

import account.ActivateAccountRequest;
import account.ChangeAccountPasswordRequest;
import account.ChangeAccountPasswordResponse;
import account.LoginRequest;
import account.LoginResponse;
import account.RegisterAccountRequest;
import account.RegisterAccountResponse;
import account.ResetAccountPasswordRequest;
import account.ResetAccountPasswordResponse;
import account.base.BaseGrpcTest;
import account.support.TestDataGenerator;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResetPasswordTest extends BaseGrpcTest {

    @Test
    void resetPasswordFlowShouldAllowLoginWithNewPassword() {
        String login = TestDataGenerator.randomLogin();
        String email = TestDataGenerator.randomEmail();
        String password = TestDataGenerator.randomPassword();
        String newPassword = TestDataGenerator.randomPassword();

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

        blockingStub.activateAccount(
                ActivateAccountRequest.newBuilder()
                        .setActivationToken(activationToken)
                        .build()
        );

        ResetAccountPasswordRequest resetRequest = ResetAccountPasswordRequest.newBuilder()
                .setLogin(login)
                .setEmail(email)
                .build();

        ResetAccountPasswordResponse resetResponse = blockingStub.resetAccountPassword(resetRequest);

        assertNotNull(resetResponse);
        assertTrue(resetResponse.hasUser());
        assertTrue(resetResponse.getUser().hasResource());
        assertEquals(login, resetResponse.getUser().getResource().getLogin());

        String resetToken = mailSteps.waitForResetPasswordToken(login);

        ChangeAccountPasswordRequest changePasswordRequest = ChangeAccountPasswordRequest.newBuilder()
                .setLogin(login)
                .setToken(StringValue.of(resetToken))
                .setOldPassword("")
                .setNewPassword(newPassword)
                .build();

        ChangeAccountPasswordResponse changePasswordResponse =
                blockingStub.changeAccountPassword(changePasswordRequest);

        assertNotNull(changePasswordResponse);
        assertTrue(changePasswordResponse.hasUser());
        assertTrue(changePasswordResponse.getUser().hasResource());
        assertEquals(login, changePasswordResponse.getUser().getResource().getLogin());

        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setLogin(login)
                .setPassword(newPassword)
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
