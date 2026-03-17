package account.tests;

import account.GetCurrentAccountRequest;
import account.GetCurrentAccountResponse;
import account.LogoutRequest;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutPositiveTest extends BaseGrpcTest {

    @Test
    void logoutShouldInvalidateToken() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        String token = user.token();

        GetCurrentAccountResponse currentAccountResponse = blockingStub.getCurrentAccount(
                GetCurrentAccountRequest.newBuilder()
                        .setToken(token)
                        .build()
        );

        assertNotNull(currentAccountResponse);
        assertTrue(currentAccountResponse.hasUser());
        assertTrue(currentAccountResponse.getUser().hasResource());
        assertEquals(
                user.login(),
                currentAccountResponse.getUser().getResource().getLogin()
        );

        assertDoesNotThrow(() ->
                blockingStub.logout(
                        LogoutRequest.newBuilder()
                                .setToken(token)
                                .build()
                )
        );

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> blockingStub.getCurrentAccount(
                        GetCurrentAccountRequest.newBuilder()
                                .setToken(token)
                                .build()
                )
        );

        assertEquals(Status.UNAUTHENTICATED.getCode(), exception.getStatus().getCode());
    }
}