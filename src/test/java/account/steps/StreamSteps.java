package account.steps;

import account.AccountServiceGrpc;
import account.GetAccountsByLoginRequest;
import account.GetAccountsByLoginResponse;
import account.RegisterAccountClientStreamResponse;
import account.RegisterAccountRequest;
import account.support.GrpcTestStreamObserver;
import io.grpc.stub.StreamObserver;

public class StreamSteps {

    private final AccountServiceGrpc.AccountServiceStub asyncStub;

    public StreamSteps(AccountServiceGrpc.AccountServiceStub asyncStub) {
        this.asyncStub = asyncStub;
    }

    public StreamObserver<GetAccountsByLoginRequest> openGetAccountsByLoginDuplexStream(
            GrpcTestStreamObserver<GetAccountsByLoginResponse> responseObserver
    ) {
        return asyncStub.getAccountsByLoginDuplexStream(responseObserver);
    }

    public StreamObserver<RegisterAccountRequest> openRegisterAccountClientStream(
            GrpcTestStreamObserver<RegisterAccountClientStreamResponse> responseObserver
    ) {
        return asyncStub.registerAccountClientStream(responseObserver);
    }
}