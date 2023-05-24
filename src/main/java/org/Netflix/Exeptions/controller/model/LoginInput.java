package org.Netflix.Exeptions.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class LoginInput {
    private String email;
    private String password;
}
