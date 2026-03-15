package account.tests;

import account.LogoutAllRequest;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LogoutAllPositiveTest extends BaseGrpcTest {

    @Test
    void logoutAllShouldInvalidateAllUserSessions() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        assertDoesNotThrow(() ->
                blockingStub.logoutAll(
                        LogoutAllRequest.newBuilder()
                                .setToken(user.token())
                                .build()
                )
        );
    }
}
