package com.webrecruiter.jwtsecurity;

import com.webrecruiter.model.User;
import com.webrecruiter.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:contants_and_other_messages.properties")
public class JwtValidator {
    
    Logger logger = Logger.getLogger(JwtValidator.class.getName());
    
    @Autowired
    UserRepository usersRepository;
    
    @Value("${tokenSecret}")
    private String secret;

    public User validate(String token) {

        User user = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            user = new User();

            user.setUserName(body.getSubject());
            user.setRole((String) body.get("userRole"));
            user.setId(user.getUserName());
            
            if (!usersRepository.existsById(user.getId())) {
                return null;
            }
        }
        catch (ExpiredJwtException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException("ceva nu a mers");
        }

        return user;
    }
}
