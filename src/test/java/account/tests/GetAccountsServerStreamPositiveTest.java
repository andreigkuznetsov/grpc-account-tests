package account.tests;

import account.User;
import account.base.BaseGrpcTest;
import account.model.TestUser;
import com.google.protobuf.Empty;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsServerStreamPositiveTest extends BaseGrpcTest {

    @Test
    void getAccountsServerStreamShouldReturnStreamIncludingCreatedUser() {
        TestUser user = userFlowSteps.registerActivateAndLogin();

        Iterator<User> iterator = blockingStub.getAccountsServerStream(Empty.getDefaultInstance());

        assertNotNull(iterator);
        assertTrue(iterator.hasNext(), "Server stream should return at least one user");

        boolean found = false;
        int limit = 200;

        while (iterator.hasNext() && limit-- > 0) {
            User currentUser = iterator.next();
            if (user.login().equals(currentUser.getLogin())) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Created user should be present in server stream");
    }
}