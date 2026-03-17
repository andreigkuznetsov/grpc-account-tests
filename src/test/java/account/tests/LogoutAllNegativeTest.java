package account.tests;

import account.LogoutAllRequest;
import account.base.BaseGrpcTest;
import account.support.TestDataGenerator;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogoutAllNegativeTest extends BaseGrpcTest {

    @Test
    void logoutAllShouldReturnInvalidArgumentForEmptyToken() {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> blockingStub.logoutAll(
                        LogoutAllRequest.newBuilder()
                                .setToken(TestDataGenerator.emptyAuthToken())
                                .build()
                )
        );

        assertEquals(Status.UNAUTHENTICATED.getCode(), exception.getStatus().getCode());
    }

    @Test
    void logoutAllShouldReturnInvalidArgumentWhenTokenNotProvided() {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> blockingStub.logoutAll(
                        LogoutAllRequest.newBuilder()
                                .build()
                )
        );

        assertEquals(Status.UNAUTHENTICATED.getCode(), exception.getStatus().getCode());
    }

    @Test
    void logoutAllShouldReturnUnauthenticatedForInvalidToken() {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> blockingStub.logoutAll(
                        LogoutAllRequest.newBuilder()
                                .setToken(TestDataGenerator.invalidAuthToken())
                                .build()
                )
        );

        assertEquals(Status.UNAUTHENTICATED.getCode(), exception.getStatus().getCode());
    }
}