package account.tests;

import account.RegisterAccountRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import account.support.TestDataGenerator;
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

    @Test
    void registerAccountShouldReturnInvalidArgumentWhenEmailIsBlank() {
        TestUser user = TestUser.withEmail("");

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

    @Test
    void registerAccountShouldReturnInvalidArgumentWhenPasswordIsBlank() {
        TestUser user = TestUser.withPassword(TestDataGenerator.emptyPassword());

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

    @Test
    void registerAccountShouldReturnInvalidArgumentWhenEmailIsInvalid() {
        TestUser user = TestUser.invalidEmailUser();

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

    @Test
    void registerAccountShouldReturnInvalidArgumentWhenPasswordIsInvalid() {
        TestUser user = TestUser.invalidPasswordUser();

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

    @Test
    void registerAccountShouldReturnAlreadyExistsWhenLoginAlreadyExists() {
        TestUser user = TestUser.random();

        blockingStub.registerAccount(
                RegisterAccountRequest.newBuilder()
                        .setLogin(user.login())
                        .setEmail(user.email())
                        .setPassword(user.password())
                        .build()
        );

        RegisterAccountRequest duplicateRequest = RegisterAccountRequest.newBuilder()
                .setLogin(user.login())
                .setEmail(TestDataGenerator.randomEmail())
                .setPassword(TestDataGenerator.randomPassword())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.registerAccount(duplicateRequest)
        );
    }

    @Test
    void registerAccountShouldReturnAlreadyExistsWhenEmailAlreadyExists() {
        TestUser user = TestUser.random();

        blockingStub.registerAccount(
                RegisterAccountRequest.newBuilder()
                        .setLogin(user.login())
                        .setEmail(user.email())
                        .setPassword(user.password())
                        .build()
        );

        RegisterAccountRequest duplicateRequest = RegisterAccountRequest.newBuilder()
                .setLogin(TestDataGenerator.randomLogin())
                .setEmail(user.email())
                .setPassword(TestDataGenerator.randomPassword())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.registerAccount(duplicateRequest)
        );
    }
}