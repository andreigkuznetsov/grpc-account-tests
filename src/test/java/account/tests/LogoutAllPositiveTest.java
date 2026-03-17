package account.tests;

import account.LogoutAllRequest;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import org.junit.jupiter.api.Test;
import account.GetCurrentAccountRequest;
import account.GetCurrentAccountResponse;
import account.LoginRequest;
import account.LoginResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void logoutAllShouldInvalidateOtherUserSessionsButKeepCurrentSessionActive() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        String currentToken = user.token();

        LoginResponse secondLoginResponse = blockingStub.login(
                LoginRequest.newBuilder()
                        .setLogin(user.login())
                        .setPassword(user.password())
                        .setRememberMe(true)
                        .build()
        );

        String secondToken = secondLoginResponse.getToken();

        assertFalse(secondToken.isBlank(), "Second token should not be blank");

        GetCurrentAccountResponse currentSessionResponseBeforeLogoutAll = blockingStub.getCurrentAccount(
                GetCurrentAccountRequest.newBuilder()
                        .setToken(currentToken)
                        .build()
        );

        assertNotNull(currentSessionResponseBeforeLogoutAll);
        assertTrue(currentSessionResponseBeforeLogoutAll.hasUser());
        assertTrue(currentSessionResponseBeforeLogoutAll.getUser().hasResource());
        assertEquals(
                user.login(),
                currentSessionResponseBeforeLogoutAll.getUser().getResource().getLogin(),
                "Current session token should be valid before logoutAll"
        );

        GetCurrentAccountResponse secondSessionResponseBeforeLogoutAll = blockingStub.getCurrentAccount(
                GetCurrentAccountRequest.newBuilder()
                        .setToken(secondToken)
                        .build()
        );

        assertNotNull(secondSessionResponseBeforeLogoutAll);
        assertTrue(secondSessionResponseBeforeLogoutAll.hasUser());
        assertTrue(secondSessionResponseBeforeLogoutAll.getUser().hasResource());
        assertEquals(
                user.login(),
                secondSessionResponseBeforeLogoutAll.getUser().getResource().getLogin(),
                "Second session token should be valid before logoutAll"
        );

        assertDoesNotThrow(() ->
                blockingStub.logoutAll(
                        LogoutAllRequest.newBuilder()
                                .setToken(currentToken)
                                .build()
                )
        );

        GetCurrentAccountResponse currentSessionResponseAfterLogoutAll = blockingStub.getCurrentAccount(
                GetCurrentAccountRequest.newBuilder()
                        .setToken(currentToken)
                        .build()
        );

        assertNotNull(currentSessionResponseAfterLogoutAll);
        assertTrue(currentSessionResponseAfterLogoutAll.hasUser());
        assertTrue(currentSessionResponseAfterLogoutAll.getUser().hasResource());
        assertEquals(
                user.login(),
                currentSessionResponseAfterLogoutAll.getUser().getResource().getLogin(),
                "Current session should remain active after logoutAll"
        );

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> blockingStub.getCurrentAccount(
                        GetCurrentAccountRequest.newBuilder()
                                .setToken(secondToken)
                                .build()
                )
        );

        assertEquals(Status.UNAUTHENTICATED.getCode(), exception.getStatus().getCode());
    }
}
