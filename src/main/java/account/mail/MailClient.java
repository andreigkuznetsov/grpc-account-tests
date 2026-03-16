package account.mail;

import account.config.MailConfig;
import account.dto.mail.MailMessagesResponse;
import account.support.JsonUtils;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.config.RestAssuredConfig.config;

@Slf4j
public class MailClient {

    private final MailConfig mailConfig;

    public MailClient() {
        this.mailConfig = new MailConfig();
    }

    public MailMessagesResponse getMessages(int limit) {
        log.info("Requesting mail messages: url={}, limit={}", mailConfig.getBaseUrl(), limit);

        Response response = RestAssured
                .given()
                .config(config().httpClient(
                        HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", (int) mailConfig.getTimeoutMs())
                                .setParam("http.socket.timeout", (int) mailConfig.getTimeoutMs())
                                .setParam("http.connection-manager.timeout", mailConfig.getTimeoutMs())
                ))
                .queryParam("limit", limit)
                .get(mailConfig.getBaseUrl())
                .then()
                .extract()
                .response();

        int statusCode = response.statusCode();
        String responseJson = response.asString();

        log.info("Mail response received: statusCode={}", statusCode);
        log.debug("Mail response body: {}", responseJson);

        MailMessagesResponse mailMessagesResponse = JsonUtils.fromJson(responseJson, MailMessagesResponse.class);

        int itemsCount = mailMessagesResponse != null && mailMessagesResponse.getItems() != null
                ? mailMessagesResponse.getItems().size()
                : 0;

        log.info("Mail messages parsed: itemsCount={}", itemsCount);

        return mailMessagesResponse;
    }

    public MailMessagesResponse getMessages() {
        return getMessages(mailConfig.getDefaultLimit());
    }
}