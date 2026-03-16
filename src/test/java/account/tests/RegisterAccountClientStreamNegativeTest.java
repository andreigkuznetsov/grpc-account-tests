package account.tests;

import account.RegisterAccountClientStreamResponse;
import account.RegisterAccountRequest;
import account.assertions.GrpcStreamAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import account.support.GrpcTestStreamObserver;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAccountClientStreamNegativeTest extends BaseGrpcTest {

    @Test
    void registerAccountClientStreamShouldReturnBusinessErrorForInvalidItem() throws InterruptedException {
        TestUser validUser = TestUser.random();
        TestUser invalidUser = TestUser.invalidLoginUser();

        GrpcTestStreamObserver<RegisterAccountClientStreamResponse> responseObserver =
                new GrpcTestStreamObserver<>();

        StreamObserver<RegisterAccountRequest> requestObserver =
                streamSteps.openRegisterAccountClientStream(responseObserver);

        requestObserver.onNext(
                RegisterAccountRequest.newBuilder()
                        .setLogin(validUser.login())
                        .setEmail(validUser.email())
                        .setPassword(validUser.password())
                        .build()
        );

        requestObserver.onNext(
                RegisterAccountRequest.newBuilder()
                        .setLogin(invalidUser.login())
                        .setEmail(invalidUser.email())
                        .setPassword(invalidUser.password())
                        .build()
        );

        requestObserver.onCompleted();

        assertTrue(responseObserver.await(), "Client stream should complete");

        RegisterAccountClientStreamResponse response = GrpcStreamAssertions.assertSingleResponseAndReturn(
                responseObserver,
                "Client stream should not fail with transport error",
                "Client stream should complete successfully"
        );

        assertEquals(2, response.getResultsCount(), "Response should contain two results");

        RegisterAccountClientStreamResponse.Result first = response.getResults(0);
        RegisterAccountClientStreamResponse.Result second = response.getResults(1);

        assertEquals(validUser.login(), first.getLogin());
        assertTrue(first.hasId(), "Valid streamed item should succeed");
        assertFalse(first.getId().isBlank());

        assertEquals(invalidUser.login(), second.getLogin());
        assertTrue(second.hasError(), "Invalid streamed item should return business error");
        assertFalse(second.getError().isBlank());
    }
}