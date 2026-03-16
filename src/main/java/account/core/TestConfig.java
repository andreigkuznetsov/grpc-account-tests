package account.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class TestConfig {

    private final String host;
    private final int port;

    public static TestConfig fromSystemProperties() {
        String host = System.getProperty("grpc.host", "185.185.143.231");
        int port = Integer.parseInt(System.getProperty("grpc.port", "5055"));
        return new TestConfig(host, port);
    }
}