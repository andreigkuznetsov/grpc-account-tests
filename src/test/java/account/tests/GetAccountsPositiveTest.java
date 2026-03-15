package account.tests;

import account.GetAccountsRequest;
import account.GetAccountsResponse;
import account.PagingQuery;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import com.google.protobuf.Int32Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsPositiveTest extends BaseGrpcTest {

    @Test
    void getAccountsShouldReturnListIncludingCreatedUser() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

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
        assertTrue(response.getAccountsCount() >= 1);

        boolean found = response.getAccountsList().stream()
                .anyMatch(account -> account.getLogin().equals(user.login()));

        assertTrue(found);
    }
}
