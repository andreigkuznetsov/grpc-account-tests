package account.tests;

import account.LoginRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class LoginNegativeTest extends BaseGrpcTest {

    @Test
    void loginShouldReturnUnauthenticatedForWrongPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        LoginRequest request = LoginRequest.newBuilder()
                .setLogin(user.login())
                .setPassword("wrong_password")
                .setRememberMe(true)
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.UNAUTHENTICATED,
                () -> blockingStub.login(request)
        );
    }
}
