package com.example.diaryboard.global.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class JwtProvider {

    private final String secretKey;
    private final Duration EXP_ACCESS = Duration.ofMinutes(30);
    private final Duration EXP_REFRESH = Duration.ofDays(30);

    public JwtProvider(String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateAccessToken(String subject) {
        return generateToken(subject, EXP_ACCESS);
    }

    public String generateRefreshToken(String subject) {
        return generateToken(subject, EXP_REFRESH);
    }

    private String generateToken(String subject, Duration expirationTime) {
        JWSSigner signer;
        try {
            signer = new MACSigner(secretKey);
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        }

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(expirationTime)))
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
