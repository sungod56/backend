package org.Netflix.Exeptions.controller;


import org.Netflix.Exeptions.InvalidCredentialsException;
import org.Netflix.Exeptions.controller.model.LoginInput;
import org.Netflix.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String>login(@RequestBody LoginInput loginInput){
        String email = loginInput.getEmail();
        String password = loginInput.getPassword();

        try{
            String token = authService.login(email, password);
            return ResponseEntity.status(HttpStatus.OK).body(token);
            
        }
        catch (InvalidCredentialsException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());


        }
    }
}
