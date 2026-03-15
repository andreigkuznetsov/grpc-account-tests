package account.tests;

import account.ResetAccountPasswordRequest;
import account.ResetAccountPasswordResponse;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResetAccountPasswordPositiveTest extends BaseGrpcTest {

    @Test
    void resetAccountPasswordShouldSendResetMail() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ResetAccountPasswordRequest request = ResetAccountPasswordRequest.newBuilder()
                .setLogin(user.login())
                .setEmail(user.email())
                .build();

        ResetAccountPasswordResponse response = blockingStub.resetAccountPassword(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());
        assertEquals(user.login(), response.getUser().getResource().getLogin());

        String resetToken = mailSteps.waitForResetPasswordToken(user.login());

        assertNotNull(resetToken);
        assertFalse(resetToken.isBlank());
    }
}
