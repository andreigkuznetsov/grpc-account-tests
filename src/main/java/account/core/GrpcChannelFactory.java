package account.core;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public final class GrpcChannelFactory {

    private GrpcChannelFactory() {
    }

    public static ManagedChannel createPlainTextChannel(String host, int port) {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }
}