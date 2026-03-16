package account.tests;

import account.GetAccountsRequest;
import account.GetAccountsResponse;
import account.PagingQuery;
import account.User;
import account.base.BaseGrpcTest;
import com.google.protobuf.Int32Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsPositiveTest extends BaseGrpcTest {

    @Test
    void getAccountsShouldReturnNonEmptyResponse() {
        PagingQuery paging = PagingQuery.newBuilder()
                .setSize(Int32Value.of(50))
                .setNumber(Int32Value.of(1))
                .build();

        GetAccountsRequest request = GetAccountsRequest.newBuilder()
                .setWithInactive(true)
                .setPaging(paging)
                .build();

        GetAccountsResponse response = blockingStub.getAccounts(request);

        assertNotNull(response);
        assertTrue(response.getAccountsCount() > 0, "Accounts list should not be empty");

        User firstUser = response.getAccounts(0);
        assertNotNull(firstUser);
        assertFalse(firstUser.getLogin().isBlank(), "User login should not be blank");

        if (response.hasPaging()) {
            assertTrue(response.getPaging().getPageSize() >= 0);
            assertTrue(response.getPaging().getCurrentPage() >= 0);
        }
    }
}