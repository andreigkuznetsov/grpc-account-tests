package account.base;

import account.AccountServiceGrpc;
import account.client.AccountClient;
import account.core.TestConfig;
import account.steps.MailSteps;
import account.steps.UserFlowSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseGrpcTest {

    protected AccountClient client;
    protected AccountServiceGrpc.AccountServiceBlockingStub blockingStub;
    protected AccountServiceGrpc.AccountServiceStub asyncStub;

    protected MailSteps mailSteps;
    protected UserFlowSteps userFlowSteps;

    @BeforeEach
    void setUpBase() {
        TestConfig config = TestConfig.fromSystemProperties();

        client = new AccountClient(config.getHost(), config.getPort());

        blockingStub = client.getBlockingStub();
        asyncStub = client.getAsyncStub();

        mailSteps = new MailSteps();
        userFlowSteps = new UserFlowSteps(blockingStub, mailSteps);
    }

    @AfterEach
    void tearDownBase() {
        if (client != null) {
            client.shutdown();
        }
    }
}