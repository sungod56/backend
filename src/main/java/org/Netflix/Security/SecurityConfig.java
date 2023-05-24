package org.Netflix.Security;

import org.Netflix.accessor.UserAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private UserAccessor userAccessor;
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService);
    }

    protected void configure(HttpSecurity http){

        
    }
}
