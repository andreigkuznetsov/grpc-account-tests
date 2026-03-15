package account.steps;

import account.mail.MailService;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public class MailSteps {

    private static final Logger log = LoggerFactory.getLogger(MailSteps.class);

    private final MailService mailService;

    public MailSteps() {
        this.mailService = new MailService();
    }

    public String waitForActivationToken(String login) {
        return waitForActivationToken(login, Duration.ofSeconds(30), Duration.ofSeconds(2));
    }

    public String waitForActivationToken(String login, Duration timeout, Duration pollInterval) {
        final String[] tokenHolder = new String[1];

        log.info("Waiting for activation token: login={}, timeout={}, pollInterval={}", login, timeout, pollInterval);

        await()
                .atMost(timeout)
                .pollInterval(pollInterval)
                .ignoreExceptions()
                .untilAsserted(() -> {
                    String token = mailService.findActivationTokenByLogin(login);
                    Assertions.assertThat(token).isNotBlank();
                    tokenHolder[0] = token;
                });

        log.info("Activation token obtained for login={}", login);
        return tokenHolder[0];
    }

    public String waitForResetPasswordToken(String login) {
        return waitForResetPasswordToken(login, Duration.ofSeconds(30), Duration.ofSeconds(2));
    }

    public String waitForResetPasswordToken(String login, Duration timeout, Duration pollInterval) {
        final String[] tokenHolder = new String[1];

        log.info("Waiting for reset password token: login={}, timeout={}, pollInterval={}", login, timeout, pollInterval);

        await()
                .atMost(timeout)
                .pollInterval(pollInterval)
                .ignoreExceptions()
                .untilAsserted(() -> {
                    String token = mailService.findResetPasswordTokenByLogin(login);
                    Assertions.assertThat(token).isNotBlank();
                    tokenHolder[0] = token;
                });

        log.info("Reset password token obtained for login={}", login);
        return tokenHolder[0];
    }
}