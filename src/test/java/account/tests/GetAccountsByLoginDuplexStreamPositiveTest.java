package account.tests;

import account.GetAccountsByLoginRequest;
import account.GetAccountsByLoginResponse;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsByLoginDuplexStreamPositiveTest extends BaseGrpcTest {

    @Test
    void getAccountsByLoginDuplexStreamShouldReturnUserForExistingLogin() throws InterruptedException {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        List<GetAccountsByLoginResponse> responses = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GetAccountsByLoginResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(GetAccountsByLoginResponse value) {
                responses.add(value);
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

        StreamObserver<GetAccountsByLoginRequest> requestObserver =
                asyncStub.getAccountsByLoginDuplexStream(responseObserver);

        requestObserver.onNext(
                GetAccountsByLoginRequest.newBuilder()
                        .setLogin(user.login())
                        .build()
        );
        requestObserver.onCompleted();

        boolean completed = latch.await(10, TimeUnit.SECONDS);

        assertTrue(completed, "Bidi stream should complete");
        assertFalse(responses.isEmpty(), "Bidi stream should return at least one response");

        GetAccountsByLoginResponse firstResponse = responses.get(0);

        assertEquals(user.login(), firstResponse.getLogin());
        assertTrue(firstResponse.hasUser(), "Response should contain user");
        assertEquals(user.login(), firstResponse.getUser().getLogin());
    }
}