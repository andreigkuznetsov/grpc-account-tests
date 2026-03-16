package account.tests;

import account.ChangeAccountEmailRequest;
import account.ChangeAccountEmailResponse;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import account.support.TestDataGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeAccountEmailPositiveTest extends BaseGrpcTest {

    @Test
    void changeAccountEmailShouldUpdateEmailForExistingUser() {
        TestUser user = userFlowSteps.registerActivateAndLogin();
        String newEmail = TestDataGenerator.newEmail();

        ChangeAccountEmailRequest request = ChangeAccountEmailRequest.newBuilder()
                .setLogin(user.login())
                .setPassword(user.password())
                .setEmail(newEmail)
                .build();

        ChangeAccountEmailResponse response = blockingStub.changeAccountEmail(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());
        assertEquals(user.login(), response.getUser().getResource().getLogin());
    }

    @Test
    void changeAccountEmailShouldAllowDuplicateEmail() {
        TestUser firstUser = userFlowSteps.registerActivateAndLogin();
        TestUser secondUser = userFlowSteps.registerActivateAndLogin();

        ChangeAccountEmailRequest request = ChangeAccountEmailRequest.newBuilder()
                .setLogin(secondUser.login())
                .setPassword(secondUser.password())
                .setEmail(firstUser.email())
                .build();

        ChangeAccountEmailResponse response = blockingStub.changeAccountEmail(request);

        assertNotNull(response);
        assertTrue(response.hasUser());
        assertTrue(response.getUser().hasResource());
        assertEquals(secondUser.login(), response.getUser().getResource().getLogin());
    }
}
