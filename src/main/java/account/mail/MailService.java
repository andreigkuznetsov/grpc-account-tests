package account.mail;

import account.dto.mail.ActivationMailBody;
import account.dto.mail.MailMessagesResponse;
import account.dto.mail.ResetPasswordMailBody;
import account.support.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final Pattern UUID_IN_PATH_PATTERN =
            Pattern.compile("([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})");

    private final MailClient mailClient;

    public MailService() {
        this.mailClient = new MailClient();
    }

    public String findActivationTokenByLogin(String login) {
        log.info("Searching activation token for login={}", login);

        List<MailMessagesResponse.MailItem> messages = getMessages();

        String token = messages.stream()
                .sorted(Comparator.comparing(
                        MailMessagesResponse.MailItem::getCreated,
                        Comparator.nullsLast(String::compareTo)
                ).reversed())
                .map(this::extractBody)
                .filter(body -> containsLogin(body, login))
                .peek(body -> log.debug("Activation mail candidate found for login={}", login))
                .map(this::extractActivationTokenFromBody)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Activation token not found for login: " + login));

        log.info("Activation token found for login={}", login);
        return token;
    }

    public String findResetPasswordTokenByLogin(String login) {
        log.info("Searching reset password token for login={}", login);

        List<MailMessagesResponse.MailItem> messages = getMessages();

        String token = messages.stream()
                .sorted(Comparator.comparing(
                        MailMessagesResponse.MailItem::getCreated,
                        Comparator.nullsLast(String::compareTo)
                ).reversed())
                .map(this::extractBody)
                .filter(body -> containsLogin(body, login))
                .peek(body -> log.debug("Reset password mail candidate found for login={}", login))
                .map(this::extractResetPasswordTokenFromBody)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Reset password token not found for login: " + login));

        log.info("Reset password token found for login={}", login);
        return token;
    }

    private List<MailMessagesResponse.MailItem> getMessages() {
        MailMessagesResponse response = mailClient.getMessages();

        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            log.warn("Mail service returned no messages");
            throw new IllegalStateException("Mail service returned no messages");
        }

        log.info("Mail service returned {} messages", response.getItems().size());

        return response.getItems().stream()
                .filter(item -> item != null)
                .toList();
    }

    private String extractBody(MailMessagesResponse.MailItem item) {
        if (item == null || item.getContent() == null) {
            return null;
        }
        return item.getContent().getBody();
    }

    private boolean containsLogin(String body, String login) {
        if (body == null || body.isBlank() || login == null || login.isBlank()) {
            return false;
        }

        return body.contains("\"Login\":\"" + login + "\"")
                || body.contains("\"login\":\"" + login + "\"")
                || body.contains("\"Login\": \"" + login + "\"")
                || body.contains("\"login\": \"" + login + "\"")
                || body.contains(login);
    }

    public String extractActivationTokenFromBody(String body) {
        if (body == null || body.isBlank()) {
            throw new IllegalStateException("Mail body is blank");
        }

        try {
            ActivationMailBody mailBody = JsonUtils.fromJson(body, ActivationMailBody.class);
            if (mailBody != null
                    && mailBody.getConfirmationLinkUrl() != null
                    && !mailBody.getConfirmationLinkUrl().isBlank()) {
                log.debug("Extracting activation token from ConfirmationLinkUrl");
                return extractUuid(mailBody.getConfirmationLinkUrl())
                        .orElseThrow(() -> new IllegalStateException("Activation token not found in ConfirmationLinkUrl"));
            }
        } catch (Exception e) {
            log.debug("Failed to parse activation mail as structured JSON, fallback to regex");
        }

        log.debug("Extracting activation token by regex from raw mail body");
        return extractUuid(body)
                .orElseThrow(() -> new IllegalStateException("Activation token not found in mail body"));
    }

    public String extractResetPasswordTokenFromBody(String body) {
        if (body == null || body.isBlank()) {
            throw new IllegalStateException("Mail body is blank");
        }

        try {
            ResetPasswordMailBody mailBody = JsonUtils.fromJson(body, ResetPasswordMailBody.class);
            if (mailBody != null
                    && mailBody.getConfirmationLinkUri() != null
                    && !mailBody.getConfirmationLinkUri().isBlank()) {
                log.debug("Extracting reset password token from ConfirmationLinkUri");
                return extractUuid(mailBody.getConfirmationLinkUri())
                        .orElseThrow(() -> new IllegalStateException("Reset password token not found in ConfirmationLinkUri"));
            }
        } catch (Exception e) {
            log.debug("Failed to parse reset mail as structured JSON, fallback to regex");
        }

        log.debug("Extracting reset password token by regex from raw mail body");
        return extractUuid(body)
                .orElseThrow(() -> new IllegalStateException("Reset password token not found in mail body"));
    }

    private Optional<String> extractUuid(String source) {
        Matcher matcher = UUID_IN_PATH_PATTERN.matcher(source);
        if (matcher.find()) {
            return Optional.ofNullable(matcher.group(1));
        }
        return Optional.empty();
    }
}