package account.tests;

import account.LogoutRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class LogoutNegativeTest extends BaseGrpcTest {

    @Test
    void logoutShouldReturnUnauthenticatedForInvalidToken() {
        LogoutRequest request = LogoutRequest.newBuilder()
                .setToken("invalid_token")
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.UNAUTHENTICATED,
                () -> blockingStub.logout(request)
        );
    }
}
