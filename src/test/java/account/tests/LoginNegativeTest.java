package account.tests;

import account.RegisterAccountRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import account.support.TestDataGenerator;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class LoginNegativeTest extends BaseGrpcTest {

    @Test
    void loginShouldReturnFailedPreconditionForWrongPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.FAILED_PRECONDITION,
                () -> blockingStub.login(
                        user.toLoginRequestWithPassword(TestDataGenerator.wrongPassword(), true)
                )
        );
    }

    @Test
    void loginShouldReturnFailedPreconditionForNotActivatedAccount() {
        TestUser user = TestUser.random();

        blockingStub.registerAccount(
                RegisterAccountRequest.newBuilder()
                        .setLogin(user.login())
                        .setEmail(user.email())
                        .setPassword(user.password())
                        .build()
        );

        GrpcAssertions.assertGrpcStatus(
                Status.Code.UNAUTHENTICATED,
                () -> blockingStub.login(
                        user.toLoginRequest(true)
                )
        );
    }

    @Test
    void loginShouldReturnFailedPreconditionForUnknownUser() {
        TestUser user = TestUser.withLogin(TestDataGenerator.unknownLogin());

        GrpcAssertions.assertGrpcStatus(
                Status.Code.FAILED_PRECONDITION,
                () -> blockingStub.login(
                        user.toLoginRequest(true)
                )
        );
    }

    @Test
    void loginShouldReturnInvalidArgumentForBlankLogin() {
        TestUser user = TestUser.invalidLoginUser();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.FAILED_PRECONDITION,
                () -> blockingStub.login(
                        user.toLoginRequest(true)
                )
        );
    }

    @Test
    void loginShouldReturnInvalidArgumentForBlankPassword() {
        TestUser user = TestUser.withPassword(TestDataGenerator.emptyPassword());

        GrpcAssertions.assertGrpcStatus(
                Status.Code.FAILED_PRECONDITION,
                () -> blockingStub.login(
                        user.toLoginRequest(true)
                )
        );
    }

    @Test
    void loginShouldReturnFailedPreconditionForInvalidPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.FAILED_PRECONDITION,
                () -> blockingStub.login(
                        user.toLoginRequestWithPassword(TestDataGenerator.invalidPassword(), true)
                )
        );
    }
}