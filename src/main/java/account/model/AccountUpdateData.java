package account.model;

import account.UserUpdate;
import account.support.TestDataGenerator;
import com.google.protobuf.StringValue;

public record AccountUpdateData(
        String status,
        String name,
        String location
) {

    public static AccountUpdateData random() {
        return new AccountUpdateData(
                TestDataGenerator.randomStatus(),
                TestDataGenerator.randomName(),
                TestDataGenerator.randomLocation()
        );
    }

    public UserUpdate toUserUpdate() {
        return UserUpdate.newBuilder()
                .setStatus(StringValue.of(status))
                .setName(StringValue.of(name))
                .setLocation(StringValue.of(location))
                .build();
    }
}