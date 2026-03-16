package account.tests;

import account.GetAccountsByLoginRequest;
import account.GetAccountsByLoginResponse;
import account.assertions.GrpcStreamAssertions;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import account.support.GrpcTestStreamObserver;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsByLoginDuplexStreamPositiveTest extends BaseGrpcTest {

    @Test
    void getAccountsByLoginDuplexStreamShouldReturnUserForExistingLogin() throws InterruptedException {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        GrpcTestStreamObserver<GetAccountsByLoginResponse> responseObserver = new GrpcTestStreamObserver<>();

        StreamObserver<GetAccountsByLoginRequest> requestObserver =
                streamSteps.openGetAccountsByLoginDuplexStream(responseObserver);

        requestObserver.onNext(
                GetAccountsByLoginRequest.newBuilder()
                        .setLogin(user.login())
                        .build()
        );
        requestObserver.onCompleted();

        assertTrue(responseObserver.await(), "Bidi stream should complete");

        GetAccountsByLoginResponse firstResponse = GrpcStreamAssertions.assertAtLeastOneResponseAndReturnFirst(
                responseObserver,
                "Bidi stream should not fail with transport error",
                "Bidi stream should complete successfully",
                "Bidi stream should return at least one response"
        );

        assertEquals(user.login(), firstResponse.getLogin());
        assertTrue(firstResponse.hasUser(), "Response should contain user");
        assertEquals(user.login(), firstResponse.getUser().getLogin());
    }
}