package account.tests;

import account.ChangeAccountPasswordRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class ChangeAccountPasswordNegativeTest extends BaseGrpcTest {

    @Test
    void changeAccountPasswordShouldReturnInvalidArgumentForBadToken() {
        ChangeAccountPasswordRequest request = ChangeAccountPasswordRequest.newBuilder()
                .setLogin("unknown_user")
                .setOldPassword("")
                .setNewPassword("NewQwerty123!")
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountPassword(request)
        );
    }
}
