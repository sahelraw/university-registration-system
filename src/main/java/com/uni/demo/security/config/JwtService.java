package com.uni.demo.security.config;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.function.Function;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class JwtService {

    private static final String SECRET_KEY = "feb5c3f37247b286ea0580e08c2b2ded5734a834521cdbccae41c37795cf2f5a";
    
public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);//the subject should be the email of the user
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
 } //we will implement this method later to generate a token with extra claims such as the role of the user and other information that we want to include in the token

    //now for implementing the method that will generate a token
    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 hours
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();//generate and return the token
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); //to check if the token is valid by checking if the username in the token is the same as the username of the user details and also to check if the token is not expired
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); //to check if the token is expired by comparing the expiration date of the token with the current date
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); //to extract the expiration date from the token using the extractClaim method that we implemented earlier
    }


    private Claims extractAllClaims(String token) {
        return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody(); 
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
