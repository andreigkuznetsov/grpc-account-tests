package account.core;

public final class TestConfig {

    private final String host;
    private final int port;
    private final String login;
    private final String password;

    public TestConfig(String host, int port, String login, String password) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public static TestConfig fromSystemProperties() {
        String host = System.getProperty("grpc.host", "185.185.143.231");
        int port = Integer.parseInt(System.getProperty("grpc.port", "5055"));
        String login = System.getProperty("grpc.login", "");
        String password = System.getProperty("grpc.password", "");

        return new TestConfig(host, port, login, password);
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public String login() {
        return login;
    }

    public String password() {
        return password;
    }
}