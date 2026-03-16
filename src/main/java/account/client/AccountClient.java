package account.client;

import account.AccountServiceGrpc;
import account.core.GrpcChannelFactory;
import io.grpc.ManagedChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AccountClient {

    private final ManagedChannel channel;
    private final AccountServiceGrpc.AccountServiceBlockingStub blockingStub;
    private final AccountServiceGrpc.AccountServiceStub asyncStub;

    public AccountClient(String host, int port) {
        log.info("Creating gRPC channel: host={}, port={}", host, port);

        this.channel = GrpcChannelFactory.createPlainTextChannel(host, port);
        this.blockingStub = AccountServiceGrpc.newBlockingStub(channel);
        this.asyncStub = AccountServiceGrpc.newStub(channel);

        log.info("gRPC client initialized");
    }

    public void shutdown() {
        if (channel != null) {
            log.info("Shutting down gRPC channel");
            channel.shutdownNow();
        }
    }
}