package account.tests;

import account.base.BaseGrpcTest;
import account.assertions.GrpcAssertions;
import account.model.TestUser;
import account.support.TestDataGenerator;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class LoginNegativeTest extends BaseGrpcTest {

    @Test
    void loginShouldReturnFailedPreconditionForWrongPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.FAILED_PRECONDITION,
                () -> blockingStub.login(
                        user.toLoginRequestWithPassword(TestDataGenerator.wrongPassword(), true)
                )
        );
    }
}
