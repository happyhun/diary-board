package com.example.diaryboard.global.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.List;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private String principalClaimName = JwtClaimNames.SUB;

    @Override
    public final AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);

        for (String authority : (List<String>) jwt.getClaim("roles")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + authority));
        }

        String principalClaimValue = jwt.getClaimAsString(this.principalClaimName);
        return new JwtAuthenticationToken(jwt, authorities, principalClaimValue);
    }
}
