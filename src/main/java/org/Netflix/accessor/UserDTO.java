package org.Netflix.accessor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDTO {
    private String userID;
    private String name;
    private String email;
    private String password;
    private String phoneNo;
    private UserState state;
    private UserRole role;
}
