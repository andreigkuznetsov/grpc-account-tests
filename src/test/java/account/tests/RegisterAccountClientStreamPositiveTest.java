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

public class RegisterAccountClientStreamPositiveTest extends BaseGrpcTest {

    @Test
    void registerAccountClientStreamShouldCreateUsers() throws InterruptedException {
        TestUser firstUser = TestUser.random();
        TestUser secondUser = TestUser.random();

        GrpcTestStreamObserver<RegisterAccountClientStreamResponse> responseObserver =
                new GrpcTestStreamObserver<>();

        StreamObserver<RegisterAccountRequest> requestObserver =
                streamSteps.openRegisterAccountClientStream(responseObserver);

        requestObserver.onNext(
                RegisterAccountRequest.newBuilder()
                        .setLogin(firstUser.login())
                        .setEmail(firstUser.email())
                        .setPassword(firstUser.password())
                        .build()
        );

        requestObserver.onNext(
                RegisterAccountRequest.newBuilder()
                        .setLogin(secondUser.login())
                        .setEmail(secondUser.email())
                        .setPassword(secondUser.password())
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

        assertEquals(firstUser.login(), first.getLogin());
        assertTrue(first.hasId(), "First streamed registration should succeed");
        assertFalse(first.getId().isBlank());

        assertEquals(secondUser.login(), second.getLogin());
        assertTrue(second.hasId(), "Second streamed registration should succeed");
        assertFalse(second.getId().isBlank());
    }
}