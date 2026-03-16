package account.tests;

import account.RegisterAccountRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class RegisterAccountNegativeTest extends BaseGrpcTest {

    @Test
    void registerAccountShouldReturnInvalidArgumentWhenLoginIsBlank() {
        TestUser user = TestUser.invalidLoginUser();

        RegisterAccountRequest request = RegisterAccountRequest.newBuilder()
                .setLogin(user.login())
                .setEmail(user.email())
                .setPassword(user.password())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.registerAccount(request)
        );
    }
}
