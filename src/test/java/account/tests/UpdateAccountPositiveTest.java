package account.tests;

import account.GetCurrentAccountRequest;
import account.GetCurrentAccountResponse;
import account.UpdateAccountRequest;
import account.UpdateAccountResponse;
import account.UserDetails;
import account.UserUpdate;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import account.support.TestDataGenerator;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateAccountPositiveTest extends BaseGrpcTest {

    @Test
    void updateAccountShouldUpdateCurrentUserFields() {
        TestUser user = userFlowSteps.registerActivateAndLogin();
        String expectedStatus = TestDataGenerator.randomStatus();
        String expectedName = TestDataGenerator.randomName();
        String expectedLocation = TestDataGenerator.randomLocation();

        UserUpdate userUpdate = UserUpdate.newBuilder()
                .setStatus(StringValue.of(expectedStatus))
                .setName(StringValue.of(expectedName))
                .setLocation(StringValue.of(expectedLocation))
                .build();

        UpdateAccountRequest request = UpdateAccountRequest.newBuilder()
                .setToken(user.token())
                .setUserData(userUpdate)
                .build();

        UpdateAccountResponse response = blockingStub.updateAccount(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());

        GetCurrentAccountResponse actualResponse = blockingStub.getCurrentAccount(
                GetCurrentAccountRequest.newBuilder()
                        .setToken(user.token())
                        .build()
        );

        UserDetails actualUser = actualResponse.getUser().getResource();

        assertEquals(user.login(), actualUser.getLogin());
        assertEquals(expectedStatus, actualUser.getStatus().getValue());
        assertEquals(expectedName, actualUser.getName().getValue());
        assertEquals(expectedLocation, actualUser.getLocation().getValue());
    }
}
