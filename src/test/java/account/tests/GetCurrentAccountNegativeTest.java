package account.tests;

import account.GetCurrentAccountRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class GetCurrentAccountNegativeTest extends BaseGrpcTest {

    @Test
    void getCurrentAccountShouldReturnUnauthenticatedForInvalidToken() {
        GetCurrentAccountRequest request = GetCurrentAccountRequest.newBuilder()
                .setToken("invalid_token")
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.UNAUTHENTICATED,
                () -> blockingStub.getCurrentAccount(request)
        );
    }
}
