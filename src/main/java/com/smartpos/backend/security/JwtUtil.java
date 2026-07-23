package com.smartpos.backend.security;

import com.smartpos.backend.entity.Permission;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init(){
        this.key= Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetailsImpl userDetails){
        Map<String,Object> claims=new HashMap<>();

        List<String> roles=userDetails.getUser().getRoles().stream()
                .map(role -> role.getName().name()).collect(Collectors.toList());
        claims.put("roles",roles);

//        Set<String> permissions=userDetails.getUser().getRoles().stream()
//                .flatMap(role -> role.getPermissions().stream())
//                .map(Permission::getName)
//                .collect(Collectors.toSet());
//        claims.put("permissions",permissions);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseSignedClaims(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser().setSigningKey(key).build().parseSignedClaims(token);
            return true;
        }catch (SecurityException e){
            System.out.println("Invalid JWT signature: "+e.getMessage());
        }catch (MalformedJwtException e){
            System.out.println("Invalid JWT token: "+e.getMessage());
        }catch (ExpiredJwtException e){
            System.out.println("JWT token is expired: "+e.getMessage());
        }catch (UnsupportedJwtException e){
            System.out.println("JWT token is unsupported: "+e.getMessage());
        }catch (IllegalArgumentException e){
            System.out.println("JWT claims string is empty: "+e.getMessage());
        }
        return false;
    }
}
