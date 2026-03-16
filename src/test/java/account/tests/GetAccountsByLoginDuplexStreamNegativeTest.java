package account.tests;

import account.GetAccountsByLoginRequest;
import account.GetAccountsByLoginResponse;
import account.assertions.GrpcStreamAssertions;
import account.base.BaseGrpcTest;
import account.support.GrpcTestStreamObserver;
import account.support.TestDataGenerator;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsByLoginDuplexStreamNegativeTest extends BaseGrpcTest {

    @Test
    void getAccountsByLoginDuplexStreamShouldReturnBusinessErrorForUnknownLogin() throws InterruptedException {
        String unknownLogin = TestDataGenerator.unknownLogin();

        GrpcTestStreamObserver<GetAccountsByLoginResponse> responseObserver = new GrpcTestStreamObserver<>();

        StreamObserver<GetAccountsByLoginRequest> requestObserver =
                streamSteps.openGetAccountsByLoginDuplexStream(responseObserver);

        requestObserver.onNext(
                GetAccountsByLoginRequest.newBuilder()
                        .setLogin(unknownLogin)
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

        assertEquals(unknownLogin, firstResponse.getLogin());
        assertTrue(firstResponse.hasError(), "Response should contain business error");
        assertFalse(firstResponse.getError().isBlank(), "Error message should not be blank");
    }
}