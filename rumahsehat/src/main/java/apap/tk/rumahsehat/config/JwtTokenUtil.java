package apap.tk.rumahsehat.config;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil {
	@Value("${jwttoken.secret}")
	private String jwtTokenSecret;
	@Value("${jwttoken.expiration}")
	private long jwtTokenExpiration;

	Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	public String generateJwtToken() {
		var loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String username = loggedInUser.getName();
		return Jwts.builder()
				   .setSubject(username)
				   .setIssuedAt(new Date(System.currentTimeMillis()))
				   .setExpiration(new Date(System.currentTimeMillis() + jwtTokenExpiration))
				   .signWith(SignatureAlgorithm.HS512, jwtTokenSecret)
				   .compact();
	}
	
	public boolean validateJwtToken(String token) {
		try {
			Jwts.parser()
				.setSigningKey(jwtTokenSecret)
				.parseClaimsJws(token);
			return true;
		}catch(UnsupportedJwtException exp) {
			logger.warn("claimsJws argument does not represent Claims JWS {}", exp.getMessage());
		}catch(MalformedJwtException exp) {
			logger.warn("claimsJws string is not a valid JWS {}", exp.getMessage());
		}catch(SignatureException exp) {
			logger.warn("claimsJws JWS signature validation failed {}", exp.getMessage());
		}catch(IllegalArgumentException exp) {
			logger.warn("claimsJws string is null or empty or only whitespace {}", exp.getMessage());
		}
		return false;
	}
	
	public String getUserNameFromJwtToken(String token) {
		 var claims = Jwts.parser()
				   .setSigningKey(jwtTokenSecret)
				   .parseClaimsJws(token)
				   .getBody();
		 return claims.getSubject();
		
	}
}