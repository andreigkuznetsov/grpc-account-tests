package account.model;

public class TestUser {

    private final String login;
    private final String email;
    private final String password;
    private final String token;

    public TestUser(String login, String email, String password, String token) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public String login() {
        return login;
    }

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }

    public String token() {
        return token;
    }
}
