package account.config;

public class MailConfig {

    private final String baseUrl;
    private final long timeoutMs;
    private final int defaultLimit;

    public MailConfig() {
        this.baseUrl = PropertiesLoader.getRequired("mail.base-url");
        this.timeoutMs = PropertiesLoader.getLong("mail.timeout.ms", 30000);
        this.defaultLimit = PropertiesLoader.getInt("mail.default-limit", 50);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public long getTimeoutMs() {
        return timeoutMs;
    }

    public int getDefaultLimit() {
        return defaultLimit;
    }
}