package account.tests;

import account.GetCurrentAccountRequest;
import account.GetCurrentAccountResponse;
import account.UserDetails;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetCurrentAccountPositiveTest extends BaseGrpcTest {

    @Test
    void getCurrentAccountShouldReturnAuthorizedUser() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        GetCurrentAccountRequest request = GetCurrentAccountRequest.newBuilder()
                .setToken(user.token())
                .build();

        GetCurrentAccountResponse response = blockingStub.getCurrentAccount(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());

        UserDetails actualUser = response.getUser().getResource();

        assertEquals(user.login(), actualUser.getLogin());
    }
}
