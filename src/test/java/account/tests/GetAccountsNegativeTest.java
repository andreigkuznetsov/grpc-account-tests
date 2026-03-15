package account.tests;

import account.GetAccountsRequest;
import account.PagingQuery;
import account.assertions.GrpcAssertions;
import account.base.BaseGrpcTest;
import com.google.protobuf.Int32Value;
import io.grpc.Status;
import org.junit.jupiter.api.Test;

public class GetAccountsNegativeTest extends BaseGrpcTest {

    @Test
    void getAccountsShouldReturnInvalidArgumentForNegativePageSize() {
        PagingQuery paging = PagingQuery.newBuilder()
                .setSize(Int32Value.of(-1))
                .setNumber(Int32Value.of(1))
                .build();

        GetAccountsRequest request = GetAccountsRequest.newBuilder()
                .setWithInactive(true)
                .setPaging(paging)
                .build();

        GrpcAssertions.assertGrpcStatus(
                Status.Code.INVALID_ARGUMENT,
                () -> blockingStub.getAccounts(request)
        );
    }
}
