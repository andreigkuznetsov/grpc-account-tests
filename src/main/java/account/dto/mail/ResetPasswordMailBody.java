package account.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetPasswordMailBody {

    @JsonProperty("Login")
    private String login;

    @JsonProperty("ConfirmationLinkUri")
    private String confirmationLinkUri;

    public String getLogin() {
        return login;
    }

    public String getConfirmationLinkUri() {
        return confirmationLinkUri;
    }
}