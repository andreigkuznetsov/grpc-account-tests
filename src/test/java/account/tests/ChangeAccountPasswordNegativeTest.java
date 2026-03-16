package account.tests;

import account.ChangeAccountPasswordRequest;
import account.ResetAccountPasswordRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import account.support.TestDataGenerator;
import com.google.protobuf.StringValue;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class ChangeAccountPasswordNegativeTest extends BaseGrpcTest {

    @Test
    void changeAccountPasswordShouldReturnInvalidArgumentForUnknownLogin() {
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

    @Test
    void changeAccountPasswordShouldReturnInvalidArgumentForBlankOldPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountPasswordRequest request = ChangeAccountPasswordRequest.newBuilder()
                .setLogin(user.login())
                .setOldPassword(TestDataGenerator.emptyPassword())
                .setNewPassword(TestDataGenerator.newPassword())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountPassword(request)
        );
    }

    @Test
    void changeAccountPasswordShouldReturnInvalidArgumentForBlankNewPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountPasswordRequest request = ChangeAccountPasswordRequest.newBuilder()
                .setLogin(user.login())
                .setOldPassword(user.password())
                .setNewPassword(TestDataGenerator.emptyPassword())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountPassword(request)
        );
    }

    @Test
    void changeAccountPasswordShouldReturnInvalidArgumentForInvalidNewPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountPasswordRequest request = ChangeAccountPasswordRequest.newBuilder()
                .setLogin(user.login())
                .setOldPassword(user.password())
                .setNewPassword(TestDataGenerator.invalidPassword())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountPassword(request)
        );
    }

    @Test
    void changeAccountPasswordShouldReturnInvalidArgumentForWrongOldPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountPasswordRequest request = ChangeAccountPasswordRequest.newBuilder()
                .setLogin(user.login())
                .setOldPassword(TestDataGenerator.wrongPassword())
                .setNewPassword(TestDataGenerator.newPassword())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountPassword(request)
        );
    }

    @Test
    void changeAccountPasswordShouldReturnInvalidArgumentForInvalidResetToken() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountPasswordRequest request = ChangeAccountPasswordRequest.newBuilder()
                .setLogin(user.login())
                .setToken(StringValue.of(TestDataGenerator.invalidAuthToken()))
                .setOldPassword(TestDataGenerator.emptyPassword())
                .setNewPassword(TestDataGenerator.newPassword())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INTERNAL,
                () -> blockingStub.changeAccountPassword(request)
        );
    }

    @Test
    void changeAccountPasswordShouldReturnInvalidArgumentForExpiredOrUnusedResetFlow() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        blockingStub.resetAccountPassword(
                ResetAccountPasswordRequest.newBuilder()
                        .setLogin(user.login())
                        .setEmail(user.email())
                        .build()
        );

        ChangeAccountPasswordRequest request = ChangeAccountPasswordRequest.newBuilder()
                .setLogin(user.login())
                .setToken(StringValue.of(TestDataGenerator.invalidAuthToken()))
                .setOldPassword(TestDataGenerator.emptyPassword())
                .setNewPassword(TestDataGenerator.newPassword())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INTERNAL,
                () -> blockingStub.changeAccountPassword(request)
        );
    }
}
