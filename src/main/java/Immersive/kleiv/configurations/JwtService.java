package Immersive.kleiv.configurations;

import Immersive.kleiv.Services.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${app.expirated-at}")
    private long expiration;
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    public Map<String , String> generateToken(String username) {
        UserDetails user = customUserDetailsService.loadUserByUsername(username);
        return generateJwt(user);
    }

    private Map<String, String> generateJwt(UserDetails user) {
        Map<String, Object> claims = Map.of(
                Claims.SUBJECT, user.getUsername(),
                Claims.EXPIRATION, new Date(expiration)
        );
        String bearer = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("Bearer", bearer);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(this.getSignKey()).build().parseClaimsJws(token).getBody();
    }
    public boolean isXpired(String token) {
        Date xpiredAt = getClaim(token, Claims::getExpiration);
        return xpiredAt.before(new Date());
    }
}
