package account.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MailMessagesResponse {

    private Integer total;
    private Integer count;
    private Integer start;
    private List<MailItem> items;

    public Integer getTotal() {
        return total;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getStart() {
        return start;
    }

    public List<MailItem> getItems() {
        return items;
    }

    public static class MailItem {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Created")
        private String created;

        @JsonProperty("Content")
        private Content content;

        public String getId() {
            return id;
        }

        public String getCreated() {
            return created;
        }

        public Content getContent() {
            return content;
        }
    }

    public static class Content {

        @JsonProperty("Body")
        private String body;

        public String getBody() {
            return body;
        }
    }
}