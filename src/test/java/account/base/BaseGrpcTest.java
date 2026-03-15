package account.base;

import account.AccountServiceGrpc;
import account.client.AccountClient;
import account.core.TestConfig;
import account.steps.MailSteps;
import account.steps.UserFlowSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseGrpcTest {

    protected TestConfig config;
    protected AccountClient client;
    protected AccountServiceGrpc.AccountServiceBlockingStub blockingStub;
    protected AccountServiceGrpc.AccountServiceStub asyncStub;
    protected MailSteps mailSteps;
    protected UserFlowSteps userFlowSteps;

    @BeforeEach
    void setUpBase() {
        config = TestConfig.fromSystemProperties();
        client = new AccountClient(config.host(), config.port());
        blockingStub = client.blockingStub();
        asyncStub = client.asyncStub();
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