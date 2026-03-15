package account.tests;

import account.RegisterAccountClientStreamResponse;
import account.RegisterAccountRequest;
import account.base.BaseGrpcTest;
import account.support.TestDataGenerator;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAccountClientStreamPositiveTest extends BaseGrpcTest {

    @Test
    void registerAccountClientStreamShouldCreateUsers() throws InterruptedException {
        String login1 = TestDataGenerator.randomLogin();
        String email1 = TestDataGenerator.randomEmail();
        String password1 = TestDataGenerator.randomPassword();

        String login2 = TestDataGenerator.randomLogin();
        String email2 = TestDataGenerator.randomEmail();
        String password2 = TestDataGenerator.randomPassword();

        CountDownLatch latch = new CountDownLatch(1);
        RegisterAccountClientStreamResponse[] holder = new RegisterAccountClientStreamResponse[1];

        StreamObserver<RegisterAccountClientStreamResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(RegisterAccountClientStreamResponse value) {
                holder[0] = value;
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
                fail(t);
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        };

        StreamObserver<RegisterAccountRequest> requestObserver =
                asyncStub.registerAccountClientStream(responseObserver);

        requestObserver.onNext(
                RegisterAccountRequest.newBuilder()
                        .setLogin(login1)
                        .setEmail(email1)
                        .setPassword(password1)
                        .build()
        );

        requestObserver.onNext(
                RegisterAccountRequest.newBuilder()
                        .setLogin(login2)
                        .setEmail(email2)
                        .setPassword(password2)
                        .build()
        );

        requestObserver.onCompleted();

        boolean completed = latch.await(10, TimeUnit.SECONDS);

        assertTrue(completed, "Client stream should complete");
        assertNotNull(holder[0], "Response should be returned");
        assertEquals(2, holder[0].getResultsCount(), "Response should contain two results");

        RegisterAccountClientStreamResponse.Result first = holder[0].getResults(0);
        RegisterAccountClientStreamResponse.Result second = holder[0].getResults(1);

        assertEquals(login1, first.getLogin());
        assertTrue(first.hasId(), "First streamed registration should succeed");
        assertFalse(first.getId().isBlank());

        assertEquals(login2, second.getLogin());
        assertTrue(second.hasId(), "Second streamed registration should succeed");
        assertFalse(second.getId().isBlank());
    }
}
