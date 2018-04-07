package com.webrecruiter.jwtsecurity;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JwtSuccessHandler implements AuthenticationSuccessHandler{
    Logger logger = Logger.getLogger(JwtSuccessHandler.class.getName());
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        logger.log(Level.INFO, "Successfully Authentication");
    }
}
