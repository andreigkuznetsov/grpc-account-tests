package account.tests;

import account.ResetAccountPasswordRequest;
import account.assertions.GrpcAssertions;
import account.support.TestDataGenerator;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class ResetAccountPasswordNegativeTest extends BaseGrpcTest {

    @Test
    void resetAccountPasswordShouldReturnInvalidArgumentForUnknownUser() {
        ResetAccountPasswordRequest request = ResetAccountPasswordRequest.newBuilder()
                .setLogin(TestDataGenerator.unknownLogin())
                .setEmail(TestDataGenerator.randomEmail())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.resetAccountPassword(request)
        );
    }
}
