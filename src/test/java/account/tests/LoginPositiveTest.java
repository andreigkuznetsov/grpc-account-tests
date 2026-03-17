package account.tests;

import account.LoginRequest;
import account.User;
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

    @Test
    void loginShouldReturnTokenAndFullUserForActivatedUser() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        LoginRequest request = LoginRequest.newBuilder()
                .setLogin(user.login())
                .setPassword(user.password())
                .setRememberMe(true)
                .build();

        LoginResponse response = blockingStub.login(request);

        assertNotNull(response);
        assertFalse(response.getToken().isBlank(), "Token should not be blank");

        assertTrue(response.hasUser(), "Response should contain user");
        assertTrue(response.getUser().hasResource(), "User should contain resource");

        User returnedUser = response.getUser().getResource();

        assertEquals(user.login(), returnedUser.getLogin(), "Returned login should match test user login");

        assertFalse(returnedUser.getRolesList().isEmpty(), "Returned user should contain roles");

        assertTrue(returnedUser.hasRating(), "Returned user should contain rating");
        assertTrue(returnedUser.hasRegistration(), "Returned user should contain registration");
        assertTrue(
                returnedUser.getRegistration().hasValue(),
                "Registration should contain timestamp value"
        );
        assertTrue(
                returnedUser.getRegistration().getValue().getSeconds() > 0,
                "Registration timestamp should be initialized"
        );
    }

    @Test
    void loginShouldReturnRegistrationAndOtherBaseUserFields() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        LoginResponse response = blockingStub.login(
                LoginRequest.newBuilder()
                        .setLogin(user.login())
                        .setPassword(user.password())
                        .setRememberMe(true)
                        .build()
        );

        assertTrue(response.hasUser(), "Response should contain user");
        assertTrue(response.getUser().hasResource(), "User should contain resource");

        User returnedUser = response.getUser().getResource();

        assertEquals(user.login(), returnedUser.getLogin(), "Returned login should match test user login");
        assertFalse(returnedUser.getRolesList().isEmpty(), "Returned user should contain roles");
        assertTrue(returnedUser.hasRating(), "Returned user should contain rating");
        assertTrue(returnedUser.hasRegistration(), "Returned user should contain registration");
        assertTrue(
                returnedUser.getRegistration().hasValue(),
                "Registration should contain timestamp value"
        );
        assertTrue(
                returnedUser.getRegistration().getValue().getSeconds() > 0,
                "Registration timestamp should be initialized"
        );
    }
}
