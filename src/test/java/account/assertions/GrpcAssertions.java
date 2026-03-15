package account.assertions;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class GrpcAssertions {

    private GrpcAssertions() {
    }

    public static StatusRuntimeException assertGrpcStatus(
            Status.Code expectedCode,
            Executable executable
    ) {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                executable
        );

        assertEquals(expectedCode, exception.getStatus().getCode());
        return exception;
    }
}