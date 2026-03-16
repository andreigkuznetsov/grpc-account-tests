package account.tests;

import account.ActivateAccountRequest;
import account.ActivateAccountResponse;
import account.RegisterAccountResponse;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivateAccountPositiveTest extends BaseGrpcTest {

    @Test
    void activateAccountShouldActivateRegisteredUser() {
        TestUser user = TestUser.random();

        RegisterAccountResponse registerResponse = blockingStub.registerAccount(user.toRegisterRequest());

        assertNotNull(registerResponse);
        assertEquals(user.login(), registerResponse.getLogin());

        String activationToken = mailSteps.waitForActivationToken(user.login());

        ActivateAccountRequest request = ActivateAccountRequest.newBuilder()
                .setActivationToken(activationToken)
                .build();

        ActivateAccountResponse response = blockingStub.activateAccount(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());
        assertEquals(user.login(), response.getUser().getResource().getLogin());
    }
}