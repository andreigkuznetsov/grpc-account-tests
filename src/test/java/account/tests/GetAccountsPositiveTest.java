package account.tests;

import account.GetAccountsRequest;
import account.GetAccountsResponse;
import account.PagingQuery;
import account.User;
import account.base.BaseGrpcTest;
import com.google.protobuf.Int32Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @Test
    void getAccountsShouldIncludeInactiveAccountsWhenFlagIsTrue() {

        PagingQuery paging = PagingQuery.newBuilder()
                .setSize(Int32Value.of(50))
                .setNumber(Int32Value.of(1))
                .build();

        GetAccountsRequest requestWithoutInactive = GetAccountsRequest.newBuilder()
                .setWithInactive(false)
                .setPaging(paging)
                .build();

        GetAccountsRequest requestWithInactive = GetAccountsRequest.newBuilder()
                .setWithInactive(true)
                .setPaging(paging)
                .build();

        GetAccountsResponse responseWithoutInactive =
                blockingStub.getAccounts(requestWithoutInactive);

        GetAccountsResponse responseWithInactive =
                blockingStub.getAccounts(requestWithInactive);

        assertNotNull(responseWithoutInactive);
        assertNotNull(responseWithInactive);

        int countWithoutInactive = responseWithoutInactive.getAccountsCount();
        int countWithInactive = responseWithInactive.getAccountsCount();

        assertTrue(
                countWithInactive >= countWithoutInactive,
                "withInactive=true should return same or more accounts"
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, 5, 1",
            "5, 5, 1",
            "6, 5, 2",
            "8, 5, 2",
            "11, 5, 3"
    })
    void getAccountsShouldCalculateCurrentPageFromEntityNumber(
            int entityNumber,
            int pageSize,
            int expectedCurrentPage
    ) {
        PagingQuery paging = PagingQuery.newBuilder()
                .setNumber(Int32Value.of(entityNumber))
                .setSize(Int32Value.of(pageSize))
                .setSkip(Int32Value.of(0))
                .build();

        GetAccountsRequest request = GetAccountsRequest.newBuilder()
                .setWithInactive(false)
                .setPaging(paging)
                .build();

        GetAccountsResponse response = blockingStub.getAccounts(request);

        assertNotNull(response);
        assertTrue(response.hasPaging(), "Response should contain paging info");

        assertEquals(pageSize, response.getPaging().getPageSize());
        assertEquals(entityNumber, response.getPaging().getEntityNumber());
        assertEquals(expectedCurrentPage, response.getPaging().getCurrentPage());
    }
}