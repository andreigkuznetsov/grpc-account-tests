package account.tests;

import account.RegisterAccountRequest;
import account.RegisterAccountResponse;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAccountPositiveTest extends BaseGrpcTest {

    @Test
    void registerAccountShouldCreateNewUser() {
        TestUser user = TestUser.random();

        RegisterAccountRequest request = RegisterAccountRequest.newBuilder()
                .setLogin(user.login())
                .setEmail(user.email())
                .setPassword(user.password())
                .build();

        RegisterAccountResponse response = blockingStub.registerAccount(request);

        assertNotNull(response);
        assertFalse(response.getId().isBlank());
        assertEquals(user.login(), response.getLogin());
    }
}
