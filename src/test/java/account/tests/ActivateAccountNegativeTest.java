package account.tests;

import account.ActivateAccountRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class ActivateAccountNegativeTest extends BaseGrpcTest {

    @Test
    void activateAccountShouldReturnInvalidArgumentForInvalidToken() {
        ActivateAccountRequest request = ActivateAccountRequest.newBuilder()
                .setActivationToken("invalid-token")
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.activateAccount(request)
        );
    }
}
