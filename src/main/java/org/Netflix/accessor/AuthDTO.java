package org.Netflix.accessor;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthDTO {
       private String authID;
       private String token;
       private String UserID;
}
