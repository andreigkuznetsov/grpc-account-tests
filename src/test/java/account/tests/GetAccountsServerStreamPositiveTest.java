package account.tests;

import account.User;
import account.base.BaseGrpcTest;
import com.google.protobuf.Empty;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsServerStreamPositiveTest extends BaseGrpcTest {

    @Test
    void getAccountsServerStreamShouldReturnAtLeastOneUser() {
        Iterator<User> iterator = blockingStub.getAccountsServerStream(Empty.getDefaultInstance());

        assertNotNull(iterator);
        assertTrue(iterator.hasNext(), "Server stream should return at least one user");

        User firstUser = iterator.next();

        assertNotNull(firstUser);
        assertFalse(firstUser.getLogin().isBlank(), "Returned user login should not be blank");
    }
}