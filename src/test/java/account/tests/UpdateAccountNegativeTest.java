package account.tests;

import account.UpdateAccountRequest;
import account.UserUpdate;
import account.assertions.GrpcAssertions;
import account.support.TestDataGenerator;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class UpdateAccountNegativeTest extends BaseGrpcTest {

    @Test
    void updateAccountShouldReturnUnauthenticatedForInvalidToken() {
        UpdateAccountRequest request = UpdateAccountRequest.newBuilder()
                .setToken(TestDataGenerator.invalidAuthToken())
                .setUserData(UserUpdate.newBuilder().build())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.UNAUTHENTICATED,
                () -> blockingStub.updateAccount(request)
        );
    }
}
