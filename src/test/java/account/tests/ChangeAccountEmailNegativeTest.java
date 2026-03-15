package account.tests;

import account.ChangeAccountEmailRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class ChangeAccountEmailNegativeTest extends BaseGrpcTest {

    @Test
    void changeAccountEmailShouldReturnUnauthenticatedForWrongPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountEmailRequest request = ChangeAccountEmailRequest.newBuilder()
                .setLogin(user.login())
                .setPassword("wrong_password")
                .setEmail("new_" + user.email())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.UNAUTHENTICATED,
                () -> blockingStub.changeAccountEmail(request)
        );
    }
}
