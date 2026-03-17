package account.tests;

import account.ChangeAccountPasswordRequest;
import account.ChangeAccountPasswordResponse;
import account.LoginRequest;
import account.LoginResponse;
import account.ResetAccountPasswordRequest;
import account.User;
import account.UserEnvelope;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import account.support.TestDataGenerator;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeAccountPasswordPositiveTest extends BaseGrpcTest {

    @Test
    void changeAccountPasswordShouldAllowLoginWithNewPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();
        String newPassword = TestDataGenerator.newPassword();

        blockingStub.resetAccountPassword(
                ResetAccountPasswordRequest.newBuilder()
                        .setLogin(user.login())
                        .setEmail(user.email())
                        .build()
        );

        String resetToken = mailSteps.waitForResetPasswordToken(user.login());

        ChangeAccountPasswordRequest request = ChangeAccountPasswordRequest.newBuilder()
                .setLogin(user.login())
                .setToken(StringValue.of(resetToken))
                .setOldPassword(TestDataGenerator.emptyPassword())
                .setNewPassword(newPassword)
                .build();

        ChangeAccountPasswordResponse response = blockingStub.changeAccountPassword(request);

        assertNotNull(response);

        UserEnvelope userEnvelope = response.getUser();
        assertTrue(response.hasUser(), "Response should contain user");

        User returnedUser = userEnvelope.getResource();
        assertTrue(userEnvelope.hasResource(), "User envelope should contain resource");
        assertEquals(user.login(), returnedUser.getLogin(), "Returned login should match test user login");

        LoginResponse loginResponse = blockingStub.login(
                LoginRequest.newBuilder()
                        .setLogin(user.login())
                        .setPassword(newPassword)
                        .setRememberMe(true)
                        .build()
        );

        assertNotNull(loginResponse);
        assertFalse(loginResponse.getToken().isBlank(), "Login token should not be blank");
        assertEquals(
                user.login(),
                loginResponse.getUser().getResource().getLogin(),
                "Logged in user login should match test user login"
        );
    }
}