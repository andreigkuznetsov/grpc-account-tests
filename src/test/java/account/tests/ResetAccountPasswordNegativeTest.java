package account.tests;

import account.ResetAccountPasswordRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class ResetAccountPasswordNegativeTest extends BaseGrpcTest {

    @Test
    void resetAccountPasswordShouldReturnNotFoundForUnknownUser() {
        ResetAccountPasswordRequest request = ResetAccountPasswordRequest.newBuilder()
                .setLogin("unknown_user")
                .setEmail("unknown@mail.test")
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.resetAccountPassword(request)
        );
    }
}
