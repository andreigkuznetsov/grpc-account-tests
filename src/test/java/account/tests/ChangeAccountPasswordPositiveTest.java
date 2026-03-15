package account.tests;

import account.ChangeAccountPasswordRequest;
import account.ChangeAccountPasswordResponse;
import account.LoginRequest;
import account.LoginResponse;
import account.ResetAccountPasswordRequest;
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
        String newPassword = TestDataGenerator.randomPassword();

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
                .setOldPassword("")
                .setNewPassword(newPassword)
                .build();

        ChangeAccountPasswordResponse response = blockingStub.changeAccountPassword(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());
        assertEquals(user.login(), response.getUser().getResource().getLogin());

        LoginResponse loginResponse = blockingStub.login(
                LoginRequest.newBuilder()
                        .setLogin(user.login())
                        .setPassword(newPassword)
                        .setRememberMe(true)
                        .build()
        );

        assertFalse(loginResponse.getToken().isBlank());
        assertEquals(user.login(), loginResponse.getUser().getResource().getLogin());
    }
}
