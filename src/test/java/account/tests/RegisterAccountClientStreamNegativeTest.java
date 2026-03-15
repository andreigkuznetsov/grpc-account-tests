package account.tests;

import account.RegisterAccountClientStreamResponse;
import account.RegisterAccountRequest;
import account.base.BaseGrpcTest;
import account.support.TestDataGenerator;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAccountClientStreamNegativeTest extends BaseGrpcTest {

    @Test
    void registerAccountClientStreamShouldReturnBusinessErrorForInvalidItem() throws InterruptedException {
        String validLogin = TestDataGenerator.randomLogin();
        String validEmail = TestDataGenerator.randomEmail();
        String validPassword = TestDataGenerator.randomPassword();

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
                        .setLogin(validLogin)
                        .setEmail(validEmail)
                        .setPassword(validPassword)
                        .build()
        );

        requestObserver.onNext(
                RegisterAccountRequest.newBuilder()
                        .setLogin("")
                        .setEmail("bad@mail.test")
                        .setPassword("Qwerty123!")
                        .build()
        );

        requestObserver.onCompleted();

        boolean completed = latch.await(10, TimeUnit.SECONDS);

        assertTrue(completed, "Client stream should complete");
        assertNotNull(holder[0], "Response should be returned");
        assertEquals(2, holder[0].getResultsCount(), "Response should contain two results");

        RegisterAccountClientStreamResponse.Result first = holder[0].getResults(0);
        RegisterAccountClientStreamResponse.Result second = holder[0].getResults(1);

        assertEquals(validLogin, first.getLogin());
        assertTrue(first.hasId(), "Valid streamed item should succeed");
        assertFalse(first.getId().isBlank());

        assertEquals("", second.getLogin());
        assertTrue(second.hasError(), "Invalid streamed item should return business error");
        assertFalse(second.getError().isBlank());
    }
}
