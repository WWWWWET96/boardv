package com.example.boardv.config.auth;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;
        if(exception instanceof BadCredentialsException){
            errorMessage = "ID or password does not match.";
        } else if(exception instanceof InternalAuthenticationServiceException){
            errorMessage = "System error";
        }else if(exception instanceof UsernameNotFoundException){
            errorMessage = "Account does not exist. Please log in after registering as a member.";
        }else if(exception instanceof AuthenticationCredentialsNotFoundException){
            errorMessage = "The authentication request was rejected.";
        }else{
            errorMessage = "Login failed for unknown reason.";
        }
        setDefaultFailureUrl("/auth/login?error=true&exception="+errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
