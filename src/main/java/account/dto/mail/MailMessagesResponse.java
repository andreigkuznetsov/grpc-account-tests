package account.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MailMessagesResponse {

    private Integer total;
    private Integer count;
    private Integer start;
    private List<MailItem> items;

    @Getter
    @NoArgsConstructor
    public static class MailItem {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Created")
        private String created;

        @JsonProperty("Content")
        private Content content;
    }

    @Getter
    @NoArgsConstructor
    public static class Content {

        @JsonProperty("Body")
        private String body;
    }
}