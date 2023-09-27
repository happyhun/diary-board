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

    private final String principalClaimName = JwtClaimNames.SUB;

    @Override
    public final AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);

        if (authorities == null) {
            authorities = new ArrayList<>();
        }

        for (String authority : (List<String>) jwt.getClaim("roles")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + authority));
        }

        String principalClaimValue = jwt.getClaimAsString(this.principalClaimName);
        return new JwtAuthenticationToken(jwt, authorities, principalClaimValue);
    }
}
