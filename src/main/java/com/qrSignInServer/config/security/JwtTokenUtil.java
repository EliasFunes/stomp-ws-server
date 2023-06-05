package com.qrSignInServer.config.security;

import com.qrSignInServer.models.User;
import com.qrSignInServer.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    JWTService jwtService;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {

//        logger.info("getUsernameFromToken");
//        logger.info(token);

        return getClaimFromToken(token, Claims::getSubject);
    }

    public Long getUserIdFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        Long id = Long.parseLong(claims.get("id").toString());
        return id;
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        return doGenerateToken(claims, user.getUsername());
    }

    public String generateQRIDToken(String qrId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("qrId", qrId);
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String generateReferenceToken(String userReference) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userReference", userReference);
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String getQRIDFromToken(String tokenQR) {
        final Claims claims = getAllClaimsFromToken(tokenQR);
        return claims.get("qrId").toString();
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        //TODO: determinar si es un usuario tipo tenant
        // Si es tenant, verificar que el token no haya sido utilizado ya para obtener un QRID
        // para eso utilizar jwtService.jwtIsPresent(token);
        // si no existe guardar el token con jwtService.create(token);

        // OBS: Se puede implementar la validacion de lista negra de token aqui ya que el tenant,
        // tiene que enviar sus credenciales cada vez que desea un QRID desde su server a su cliente
        // debe hacer eso.
        // Puede loggearse para ofrecer los servicios de formulario para validar la primera vez a su user.
        // En ese caso tambien solo necesita loggearse una vez. Por lo que aplica la regla de un token a la vez.
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public Boolean validateTokenUser(String token, User user) throws ValidationException {
        if(user.getTipo().equals("tenant")){
            jwtService.create(token);
        }
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public String getReferenceFromToken(String token) throws ValidationException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            String reference = claims.get("userReference").toString();
            // Signature is valid
            return reference;
        } catch (Exception e) {
            throw  new ValidationException("JWT validation error");
        }
    }
}
