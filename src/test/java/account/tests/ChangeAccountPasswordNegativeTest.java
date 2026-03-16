package account.tests;

import account.ChangeAccountPasswordRequest;
import account.base.BaseGrpcTest;
import account.assertions.GrpcAssertions;
import account.support.TestDataGenerator;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class ChangeAccountPasswordNegativeTest extends BaseGrpcTest {

    @Test
    void changeAccountPasswordShouldReturnInvalidArgumentForInvalidCredentials() {
        ChangeAccountPasswordRequest request = ChangeAccountPasswordRequest.newBuilder()
                .setLogin(TestDataGenerator.unknownLogin())
                .setOldPassword(TestDataGenerator.emptyPassword())
                .setNewPassword(TestDataGenerator.newPassword())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountPassword(request)
        );
    }
}
