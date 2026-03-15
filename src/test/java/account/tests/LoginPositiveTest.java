package account.tests;

import account.LoginRequest;
import account.LoginResponse;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginPositiveTest extends BaseGrpcTest {

    @Test
    void loginShouldReturnTokenForActivatedUser() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        LoginRequest request = LoginRequest.newBuilder()
                .setLogin(user.login())
                .setPassword(user.password())
                .setRememberMe(true)
                .build();

        LoginResponse response = blockingStub.login(request);

        assertNotNull(response);
        assertFalse(response.getToken().isBlank());
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());
        assertEquals(user.login(), response.getUser().getResource().getLogin());
    }
}
