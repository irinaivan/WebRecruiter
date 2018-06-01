package com.webrecruiter.jwtsecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource({"classpath:http_messages.properties", "classpath:contants_and_other_messages.properties"})
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    @Value("${error.missingToken}")
    private String missingToken;
    
    @Value("${httpHeaderName}")
    private String httpHeaderName;
    
    @Value("${httpHeaderStartingConvention}")
    private String httpHeaderStartingConvention;
    
    public JwtAuthenticationTokenFilter() {
        super("/**Module/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String header = httpServletRequest.getHeader(httpHeaderName);


        if (header == null || !header.startsWith(String.format("%s ", httpHeaderStartingConvention))) {
            throw new RuntimeException(missingToken);
        }

        String authenticationToken = header.substring(6);

        JwtAuthenticationToken token = new JwtAuthenticationToken(authenticationToken);
        return getAuthenticationManager().authenticate(token);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
