package account.tests;

import account.LogoutRequest;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LogoutPositiveTest extends BaseGrpcTest {

    @Test
    void logoutShouldInvalidateToken() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        assertDoesNotThrow(() ->
                blockingStub.logout(
                        LogoutRequest.newBuilder()
                                .setToken(user.token())
                                .build()
                )
        );
    }
}
