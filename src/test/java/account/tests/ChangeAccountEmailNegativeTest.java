package account.tests;

import account.ChangeAccountEmailRequest;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import account.support.TestDataGenerator;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class ChangeAccountEmailNegativeTest extends BaseGrpcTest {

    @Test
    void changeAccountEmailShouldReturnInvalidArgumentForWrongPassword() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountEmailRequest request = ChangeAccountEmailRequest.newBuilder()
                .setLogin(user.login())
                .setPassword(TestDataGenerator.wrongPassword())
                .setEmail(TestDataGenerator.newEmailFrom(user.email()))
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountEmail(request)
        );
    }

    @Test
    void changeAccountEmailShouldReturnInvalidArgumentForUnknownLogin() {
        ChangeAccountEmailRequest request = ChangeAccountEmailRequest.newBuilder()
                .setLogin(TestDataGenerator.unknownLogin())
                .setPassword(TestDataGenerator.randomPassword())
                .setEmail(TestDataGenerator.newEmail())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountEmail(request)
        );
    }

    @Test
    void changeAccountEmailShouldReturnInvalidArgumentForBlankEmail() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountEmailRequest request = ChangeAccountEmailRequest.newBuilder()
                .setLogin(user.login())
                .setPassword(user.password())
                .setEmail("")
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountEmail(request)
        );
    }

    @Test
    void changeAccountEmailShouldReturnInvalidArgumentForInvalidEmail() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        ChangeAccountEmailRequest request = ChangeAccountEmailRequest.newBuilder()
                .setLogin(user.login())
                .setPassword(user.password())
                .setEmail(TestDataGenerator.invalidEmail())
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.changeAccountEmail(request)
        );
    }
}