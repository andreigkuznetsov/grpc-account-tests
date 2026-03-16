package account.support;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GrpcTestStreamObserver<T> implements StreamObserver<T> {

    private static final long DEFAULT_TIMEOUT_SECONDS = 10;

    private final CountDownLatch doneLatch = new CountDownLatch(1);
    private final List<T> values = new ArrayList<>();

    @lombok.Getter
    private Throwable error;
    @lombok.Getter
    private boolean completed;

    @Override
    public void onNext(T value) {
        values.add(value);
    }

    @Override
    public void onError(Throwable t) {
        error = t;
        doneLatch.countDown();
    }

    @Override
    public void onCompleted() {
        completed = true;
        doneLatch.countDown();
    }

    public boolean await() throws InterruptedException {
        return doneLatch.await(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return doneLatch.await(timeout, unit);
    }

    public List<T> getValues() {
        return Collections.unmodifiableList(values);
    }

    public T getFirstValue() {
        if (values.isEmpty()) {
            throw new IllegalStateException("No values were received from stream");
        }
        return values.getFirst();
    }

    public T getOnlyValue() {
        if (values.size() != 1) {
            throw new IllegalStateException("Expected exactly one value, but got: " + values.size());
        }
        return values.getFirst();
    }

    public boolean hasError() {
        return error != null;
    }

}
