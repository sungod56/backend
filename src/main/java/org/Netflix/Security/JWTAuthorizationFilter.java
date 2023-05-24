package org.Netflix.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.Netflix.Security.Roles;
import org.Netflix.Security.SecurityConstants;
import org.Netflix.accessor.AuthAccessor;
import org.Netflix.accessor.AuthDTO;
import org.Netflix.accessor.UserAccessor;
import org.Netflix.accessor.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;
import java.util.Date;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private AuthAccessor authAccessor;
    private UserAccessor userAccessor;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain){

        ServletContext servletContext = req.getServletContext();
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        if(userAccessor == null) {
            userAccessor = (UserAccessor) applicationContext.getBean("userAccessor");
        }

        if(authAccessor == null){
            authAccessor = (AuthAccessor) applicationContext.getBean("authAccessor");
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
        }
        catch(MalformedJwtException | BadCredentialsException | IOException | ServletException ex){
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req){
        String authorizationheader =req.getHeader("Authorization");
        final String tokenPrefix = "Bearer";
        if(authorizationheader != null){
            if(authorizationheader.startsWith(tokenPrefix)) {
                String token = authorizationheader.replace(tokenPrefix, "");
                Claims claims = Jwts.parser()
                        .setSigningKey(SecurityConstants.SECRET_KEY.getBytes())
                        .parseClaimsJws(token)
                        .getBody();
                Date expirationTime = claims.getExpiration();
                if(expirationTime.after(new Date(System.currentTimeMillis()))){
                    AuthDTO authDTO = authAccessor.getAuthByToken(token);
                    if(authDTO != null){
                        UserDTO userDTO = userAccessor.getUserByEmail(claims.getSubject());
                        if(userDTO != null){
                            return new UsernamePasswordAuthenticationToken(userDTO, userDTO.getPassword(), Arrays.asList(new SimpleGrantedAuthority(userDTO.getRole().name())));
                        }

                    }
                }



            }
        }

        return new UsernamePasswordAuthenticationToken(null, null, Arrays.asList(new SimpleGrantedAuthority(Roles.Anonymous)));
    }
}
