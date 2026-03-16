package account.tests;

import account.LogoutAllRequest;
import account.assertions.GrpcAssertions;
import account.support.TestDataGenerator;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class LogoutAllNegativeTest extends BaseGrpcTest {

    @Test
    void logoutAllShouldReturnUnauthenticatedForInvalidToken() {
        LogoutAllRequest request = LogoutAllRequest.newBuilder()
                .setToken(TestDataGenerator.invalidAuthToken())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.UNAUTHENTICATED,
                () -> blockingStub.logoutAll(request)
        );
    }
}
