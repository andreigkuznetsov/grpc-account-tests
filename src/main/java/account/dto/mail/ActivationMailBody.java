package account.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ActivationMailBody {

    @JsonProperty("Login")
    private String login;

    @JsonProperty("ConfirmationLinkUrl")
    private String confirmationLinkUrl;
}