package io.okami101.realworld.infrastructure.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.User;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultJwtService implements JwtService {
  private SecretKey secret;
  private int sessionTime;

  public DefaultJwtService(
      @Value("${jwt.secret}") String secret, @Value("${jwt.sessionTime}") int sessionTime) {
    this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.sessionTime = sessionTime;
  }

  @Override
  public String encode(User user) {

    return Jwts.builder()
        .subject(user.getId().toString())
        .expiration(expireTimeFromNow())
        .signWith(this.secret)
        .compact();
  }

  @Override
  public Optional<String> parse(String token) {
    try {
      Jws<Claims> claimsJws =
          Jwts.parser().verifyWith(this.secret).build().parseSignedClaims(token);

      return Optional.ofNullable(claimsJws.getPayload().getSubject());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private Date expireTimeFromNow() {
    return new Date(System.currentTimeMillis() + sessionTime * 1000);
  }
}
