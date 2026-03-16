package account.tests;

import account.UpdateAccountRequest;
import account.UpdateAccountResponse;
import account.UserDetails;
import account.base.BaseGrpcTest;
import account.model.AccountUpdateData;
import account.model.TestUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateAccountPositiveTest extends BaseGrpcTest {

    @Test
    void updateAccountShouldUpdateUserData() {
        TestUser user = userFlowSteps.registerActivateAndLogin();
        AccountUpdateData expectedUpdate = AccountUpdateData.random();

        UpdateAccountRequest request = UpdateAccountRequest.newBuilder()
                .setToken(user.token())
                .setUserData(expectedUpdate.toUserUpdate())
                .build();

        UpdateAccountResponse response = blockingStub.updateAccount(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());

        UserDetails updatedUser = response.getUser().getResource();

        assertEquals(user.login(), updatedUser.getLogin());
        assertEquals(expectedUpdate.status(), updatedUser.getStatus().getValue());
        assertEquals(expectedUpdate.name(), updatedUser.getName().getValue());
        assertEquals(expectedUpdate.location(), updatedUser.getLocation().getValue());
    }
}