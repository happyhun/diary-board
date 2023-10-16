package com.example.diaryboard.global.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.example.diaryboard.global.jwt.JwtConfig.*;

@RequiredArgsConstructor
public class JwtProvider {

    private final String secretKey;

    public String generateAccessToken(String subject) {
        return generateToken(subject, EXP_ACCESS, SCOPE_ACCESS, List.of(ROLE_USER));
    }

    public String generateRefreshToken(String subject) {
        return generateToken(subject, EXP_REFRESH, SCOPE_REFRESH, List.of(ROLE_USER));
    }

    private String generateToken(String subject, Duration expirationTime, String scope, List<String> roles) {
        JWSSigner signer; // JWS: JSON Web Signature

        try {
            signer = new MACSigner(secretKey); // MAC: Message Authentication Code
        } catch (KeyLengthException e) { // secretKey의 길이가 적절하지 않는 경우
            throw new RuntimeException(e);
        }

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder() // JWT payload
                .subject(subject)
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(expirationTime)))
                .claim("scp", scope)
                .claim("roles", roles)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build(), claimsSet);

        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return signedJWT.serialize();
    }
}
