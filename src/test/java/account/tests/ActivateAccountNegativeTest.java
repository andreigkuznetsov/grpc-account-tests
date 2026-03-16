package account.tests;

import account.ActivateAccountRequest;
import account.base.BaseGrpcTest;
import account.assertions.GrpcAssertions;
import account.support.TestDataGenerator;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class ActivateAccountNegativeTest extends BaseGrpcTest {

    @Test
    void activateAccountShouldReturnInvalidArgumentForInvalidToken() {
        ActivateAccountRequest request = ActivateAccountRequest.newBuilder()
                .setActivationToken(TestDataGenerator.invalidActivationToken())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INTERNAL,
                () -> blockingStub.activateAccount(request)
        );
    }
}