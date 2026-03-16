package account.config;

import lombok.Getter;

@Getter
public class MailConfig {

    private final String baseUrl;
    private final long timeoutMs;
    private final int defaultLimit;

    public MailConfig() {
        this.baseUrl = PropertiesLoader.getRequired("mail.base-url");
        this.timeoutMs = PropertiesLoader.getLong("mail.timeout.ms", 30000);
        this.defaultLimit = PropertiesLoader.getInt("mail.default-limit", 50);
    }
}