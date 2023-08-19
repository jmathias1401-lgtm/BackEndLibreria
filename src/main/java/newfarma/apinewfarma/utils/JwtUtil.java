package newfarma.apinewfarma.utils;
/*
import com.universalsoft.backoffice.user.error.UserHandleError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;*/
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


public class JwtUtil {
	@Value("${secret-key}")
	private String secretKey;

	private static final String secret = "KTpZ3dXG6KdZtkmFf+pVb+=LJYTBAQgTgNB+=76dWjM5Tik=DTLn/n/Vz=Qev/S4M224ezf+pVb+=LJYTBG6KdZtkmFf+";
	private static final long EXPIRATION_TIME = 10000 * 60 * 60 * 1; // 1 hour

	// private static final Logger log = (Logger)
	// LoggerFactory.getLogger(JwtUtil.class);
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
/*
	public String generateToken(String username) throws Exception {
		Claims claims = Jwts.claims().setSubject(username);
		Date expirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date()).setExpiration(expirationTime)
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public boolean validateToken(String token) throws Exception {
//            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
//            	return true;
		try {
			Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
			return true;
		} catch (Exception e) {
			logger.error("Token invalido, error: ".concat(e.getMessage()));
			return false;
		}
	}*/

	/*public String refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws UserHandleError, Exception {
		final String authHeader = request.getHeader("Authorization");
		final String refreshToken;
		final String userName;
		String accessToken = "";
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return null;
		}
		refreshToken = authHeader.substring(7);
		userName = extractUsername(refreshToken);
		if (userName != null) {

			if (validateToken(refreshToken)) {
				accessToken = generateToken(userName);
			}
		}
		return accessToken;
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	*/
}
