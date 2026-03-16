package account.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResetPasswordMailBody {

    @JsonProperty("Login")
    private String login;

    @JsonProperty("ConfirmationLinkUri")
    private String confirmationLinkUri;
}