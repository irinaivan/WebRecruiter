package com.webrecruiter.jwtsecurity;

import com.webrecruiter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Component
@PropertySource("classpath:http_messages.properties")
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Value("${error.incorrectToken}")
    private String incorrectToken;
    
    @Autowired
    private JwtValidator validator;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
        String token = jwtAuthenticationToken.getToken();

        User user = validator.validate(token);

        if (user == null) {
            throw new RuntimeException(incorrectToken);
        }

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(user.getRole());
        return new JwtUserDetails(user.getUserName(),
                token,
                grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
