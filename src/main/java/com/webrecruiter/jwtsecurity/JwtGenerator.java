package com.webrecruiter.jwtsecurity;

import com.webrecruiter.model.User;
import com.webrecruiter.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:contants_and_other_messages.properties")
public class JwtGenerator {
    final int msInAMinute = 60000;
    final int oneWeekInMinutes = 7*24*60;
    
    @Autowired
    UserRepository usersRepository;
    
    @Value("${tokenSecret}")
    private String secret;

        public String generate(User user) {
        if (usersRepository.existsById(user.getId())) {
            Claims claims = Jwts.claims()
                    .setSubject(user.getUserName());
            claims.put("userRole", user.getRole());


            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + oneWeekInMinutes*msInAMinute))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        } else {
            return null;
        }
    }
}
