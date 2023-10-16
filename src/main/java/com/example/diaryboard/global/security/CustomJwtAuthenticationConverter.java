package com.example.diaryboard.global.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public final AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt); // JWT에서 권한 정보를 추출 (scope claim)

        if (authorities == null) {
            authorities = new ArrayList<>();
        }

        for (String authority : (List<String>) jwt.getClaim("roles")) { // JWT에서 권한 정보를 추출 (roles claim)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + authority));
        }

        String principalClaimName = JwtClaimNames.SUB;
        String principalClaimValue = jwt.getClaimAsString(principalClaimName);
        return new JwtAuthenticationToken(jwt, authorities, principalClaimValue);
    }
}
