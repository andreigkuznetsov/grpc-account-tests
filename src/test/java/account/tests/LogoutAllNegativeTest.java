package account.tests;

import account.LogoutAllRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class LogoutAllNegativeTest extends BaseGrpcTest {

    @Test
    void logoutAllShouldReturnUnauthenticatedForInvalidToken() {
        LogoutAllRequest request = LogoutAllRequest.newBuilder()
                .setToken("invalid_token")
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.UNAUTHENTICATED,
                () -> blockingStub.logoutAll(request)
        );
    }
}
