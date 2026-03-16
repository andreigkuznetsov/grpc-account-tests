package account.assertions;

import account.support.GrpcTestStreamObserver;

import static org.junit.jupiter.api.Assertions.*;

public final class GrpcStreamAssertions {

    private GrpcStreamAssertions() {
    }

    public static <T> void assertStreamCompletedSuccessfully(GrpcTestStreamObserver<T> observer) {
        assertFalse(observer.hasError(), "Stream should not fail with transport error");
        assertTrue(observer.isCompleted(), "Stream should complete successfully");
    }

    public static <T> void assertStreamCompletedSuccessfully(
            GrpcTestStreamObserver<T> observer,
            String transportErrorMessage,
            String completionMessage
    ) {
        assertFalse(observer.hasError(), transportErrorMessage);
        assertTrue(observer.isCompleted(), completionMessage);
    }

    public static <T> T assertSingleResponseAndReturn(GrpcTestStreamObserver<T> observer) {
        assertStreamCompletedSuccessfully(observer);
        return observer.getOnlyValue();
    }

    public static <T> T assertSingleResponseAndReturn(
            GrpcTestStreamObserver<T> observer,
            String transportErrorMessage,
            String completionMessage
    ) {
        assertStreamCompletedSuccessfully(observer, transportErrorMessage, completionMessage);
        return observer.getOnlyValue();
    }

    public static <T> T assertAtLeastOneResponseAndReturnFirst(GrpcTestStreamObserver<T> observer) {
        assertStreamCompletedSuccessfully(observer);
        assertFalse(observer.getValues().isEmpty(), "Stream should return at least one response");
        return observer.getFirstValue();
    }

    public static <T> T assertAtLeastOneResponseAndReturnFirst(
            GrpcTestStreamObserver<T> observer,
            String transportErrorMessage,
            String completionMessage,
            String emptyResponseMessage
    ) {
        assertStreamCompletedSuccessfully(observer, transportErrorMessage, completionMessage);
        assertFalse(observer.getValues().isEmpty(), emptyResponseMessage);
        return observer.getFirstValue();
    }
}
