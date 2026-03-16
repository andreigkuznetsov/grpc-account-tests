package account.tests;

import account.ChangeAccountEmailRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import io.grpc.Status;
import org.junit.jupiter.api.Test;
import account.support.TestDataGenerator;

public class ChangeAccountEmailNegativeTest extends BaseGrpcTest {

    @Test
    void changeAccountEmailShouldReturnInvalidArgumentForWrongPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountEmailRequest request = ChangeAccountEmailRequest.newBuilder()
                .setLogin(user.login())
                .setPassword(TestDataGenerator.wrongPassword())
                .setEmail(TestDataGenerator.newEmailFrom(user.email()))
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountEmail(request)
        );
    }
}
