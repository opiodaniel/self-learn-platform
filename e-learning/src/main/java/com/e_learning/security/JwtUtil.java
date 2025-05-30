package com.e_learning.security;

import com.e_learning.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    //public String generateToken(UserDetails userDetails)
    public String generateToken(User userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId()); // <-- Add userId here
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Extracts Username from token
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Extract Claims
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        //System.out.println("Claims: "+ claims);
        return claimsResolvers.apply(claims); //run this function on the claims object.”
    }

    // "I take my secret key string, decode it into bytes, and convert it into a signing key for JWT."
    private Key getSignInKey() {
        byte[] key = Decoders.BASE64.decode("tYjI+JHeKX/Tl2AeyIiDfkSCNtgwbHn98BhSqTjbgYE=");
        return Keys.hmacShaKeyFor(key);
    }

    //gets all claims
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


}


