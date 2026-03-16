package account.tests;

import account.GetCurrentAccountRequest;
import account.GetCurrentAccountResponse;
import account.UpdateAccountRequest;
import account.UpdateAccountResponse;
import account.UserDetails;
import account.UserUpdate;
import account.base.BaseGrpcTest;
import account.model.AccountUpdateData;
import account.model.TestUser;
import com.google.protobuf.BoolValue;
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

    @Test
    void updateAccountShouldKeepOtherFieldsUnchangedWhenOnlyRatingDisabledIsUpdated() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        GetCurrentAccountResponse beforeResponse = blockingStub.getCurrentAccount(
                GetCurrentAccountRequest.newBuilder()
                        .setToken(user.token())
                        .build()
        );

        assertNotNull(beforeResponse);
        assertTrue(beforeResponse.hasUser());
        assertTrue(beforeResponse.getUser().hasResource());

        UserDetails beforeUser = beforeResponse.getUser().getResource();

        UpdateAccountRequest request = UpdateAccountRequest.newBuilder()
                .setToken(user.token())
                .setUserData(
                        UserUpdate.newBuilder()
                                .setRatingDisabled(BoolValue.of(true))
                                .build()
                )
                .build();

        UpdateAccountResponse response = blockingStub.updateAccount(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());

        UserDetails updatedUser = response.getUser().getResource();

        assertEquals(user.login(), updatedUser.getLogin());

        assertTrue(updatedUser.hasRating(), "Updated user should contain rating");
        assertFalse(updatedUser.getRating().getEnabled(), "rating.enabled should become false");

        assertEquals(beforeUser.hasStatus(), updatedUser.hasStatus(), "Status presence should remain unchanged");
        if (beforeUser.hasStatus()) {
            assertEquals(beforeUser.getStatus().getValue(), updatedUser.getStatus().getValue(), "Status should remain unchanged");
        }

        assertEquals(beforeUser.hasName(), updatedUser.hasName(), "Name presence should remain unchanged");
        if (beforeUser.hasName()) {
            assertEquals(beforeUser.getName().getValue(), updatedUser.getName().getValue(), "Name should remain unchanged");
        }

        assertEquals(beforeUser.hasLocation(), updatedUser.hasLocation(), "Location presence should remain unchanged");
        if (beforeUser.hasLocation()) {
            assertEquals(beforeUser.getLocation().getValue(), updatedUser.getLocation().getValue(), "Location should remain unchanged");
        }

        assertEquals(beforeUser.hasIcq(), updatedUser.hasIcq(), "ICQ presence should remain unchanged");
        if (beforeUser.hasIcq()) {
            assertEquals(beforeUser.getIcq().getValue(), updatedUser.getIcq().getValue(), "ICQ should remain unchanged");
        }

        assertEquals(beforeUser.hasSkype(), updatedUser.hasSkype(), "Skype presence should remain unchanged");
        if (beforeUser.hasSkype()) {
            assertEquals(beforeUser.getSkype().getValue(), updatedUser.getSkype().getValue(), "Skype should remain unchanged");
        }

        assertEquals(beforeUser.hasInfo(), updatedUser.hasInfo(), "Info presence should remain unchanged");
        if (beforeUser.hasInfo()) {
            assertEquals(beforeUser.getInfo().getValue(), updatedUser.getInfo().getValue(), "Info should remain unchanged");
        }
    }

    @Test
    void updateAccountShouldKeepRatingUnchangedWhenOnlyNameIsUpdated() {
        TestUser user = userFlowSteps.registerActivateAndLogin();
        String expectedName = account.support.TestDataGenerator.randomName();

        GetCurrentAccountResponse beforeResponse = blockingStub.getCurrentAccount(
                GetCurrentAccountRequest.newBuilder()
                        .setToken(user.token())
                        .build()
        );

        assertNotNull(beforeResponse);
        assertTrue(beforeResponse.hasUser());
        assertTrue(beforeResponse.getUser().hasResource());

        UserDetails beforeUser = beforeResponse.getUser().getResource();

        UpdateAccountRequest request = UpdateAccountRequest.newBuilder()
                .setToken(user.token())
                .setUserData(
                        UserUpdate.newBuilder()
                                .setName(com.google.protobuf.StringValue.of(expectedName))
                                .build()
                )
                .build();

        UpdateAccountResponse response = blockingStub.updateAccount(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());

        UserDetails updatedUser = response.getUser().getResource();

        assertEquals(user.login(), updatedUser.getLogin());
        assertTrue(updatedUser.hasName(), "Updated user should contain name");
        assertEquals(expectedName, updatedUser.getName().getValue(), "Name should be updated");

        assertEquals(beforeUser.hasRating(), updatedUser.hasRating(), "Rating presence should remain unchanged");
        if (beforeUser.hasRating()) {
            assertEquals(
                    beforeUser.getRating().getEnabled(),
                    updatedUser.getRating().getEnabled(),
                    "Rating enabled flag should remain unchanged"
            );
        }

        assertEquals(beforeUser.hasStatus(), updatedUser.hasStatus(), "Status presence should remain unchanged");
        if (beforeUser.hasStatus()) {
            assertEquals(beforeUser.getStatus().getValue(), updatedUser.getStatus().getValue(), "Status should remain unchanged");
        }

        assertEquals(beforeUser.hasLocation(), updatedUser.hasLocation(), "Location presence should remain unchanged");
        if (beforeUser.hasLocation()) {
            assertEquals(beforeUser.getLocation().getValue(), updatedUser.getLocation().getValue(), "Location should remain unchanged");
        }

        assertEquals(beforeUser.hasIcq(), updatedUser.hasIcq(), "ICQ presence should remain unchanged");
        if (beforeUser.hasIcq()) {
            assertEquals(beforeUser.getIcq().getValue(), updatedUser.getIcq().getValue(), "ICQ should remain unchanged");
        }

        assertEquals(beforeUser.hasSkype(), updatedUser.hasSkype(), "Skype presence should remain unchanged");
        if (beforeUser.hasSkype()) {
            assertEquals(beforeUser.getSkype().getValue(), updatedUser.getSkype().getValue(), "Skype should remain unchanged");
        }

        assertEquals(beforeUser.hasInfo(), updatedUser.hasInfo(), "Info presence should remain unchanged");
        if (beforeUser.hasInfo()) {
            assertEquals(beforeUser.getInfo().getValue(), updatedUser.getInfo().getValue(), "Info should remain unchanged");
        }
    }
}