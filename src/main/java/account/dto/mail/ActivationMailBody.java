package account.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActivationMailBody {

    @JsonProperty("Login")
    private String login;

    @JsonProperty("ConfirmationLinkUrl")
    private String confirmationLinkUrl;

    public String getLogin() {
        return login;
    }

    public String getConfirmationLinkUrl() {
        return confirmationLinkUrl;
    }
}