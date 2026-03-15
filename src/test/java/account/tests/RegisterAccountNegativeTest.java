package account.tests;

import account.RegisterAccountRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class RegisterAccountNegativeTest extends BaseGrpcTest {

    @Test
    void registerAccountShouldReturnInvalidArgumentWhenLoginIsBlank() {
        RegisterAccountRequest request = RegisterAccountRequest.newBuilder()
                .setLogin("")
                .setEmail("bad@mail.test")
                .setPassword("Qwerty123!")
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.registerAccount(request)
        );
    }
}
