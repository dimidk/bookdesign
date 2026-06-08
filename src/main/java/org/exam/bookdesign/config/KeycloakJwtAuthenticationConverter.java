package org.exam.bookdesign.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Component
@Slf4j
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt,  AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId ;


    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        return new JwtAuthenticationToken(source,
                Stream.concat(jwtGrantedAuthoritiesConverter.convert(source).stream(),
                                 extractResourceAuthorities(source).stream()).collect(toSet()),
                getPrincipleClainName(source));
    }

    private String getPrincipleClainName(Jwt jwt) {

        String clainName = JwtClaimNames.SUB;

        if (principleAttribute != null) {
            clainName = principleAttribute;
        }
        return jwt.getClaim(clainName);
    }


    private Collection<? extends GrantedAuthority> extractResourceAuthorities(Jwt jwt) {
        //this is the resource access that is in jwt token. Is an object having account object with a list of roles.
        var resourcesAccess = new HashMap<>(jwt.getClaim("resource_access"));

        if (resourcesAccess.get(resourceId) == null) {
            return Set.of();
        }

        Map<String,Object> resources = (Map<String, Object>) resourcesAccess.get(resourceId);

        log.info("in jwt claim resource id {}",resources.toString());

        Collection<String> resourceRoles = (Collection<String>) resources.get("roles");
        Map<String,List<String>> eternal = (Map<String, List<String>>) resourcesAccess.get("account");
//        List<String> roles = eternal.get("roles");

        log.info("in jwt roles {}",resourceRoles.toString());
    //εδώ δηλώνεται το authorities το οποίο χρησιμοποιείται στο BookUserController για να γίνει add ο χρήστης στη βάση. Έτσι ήταν παλιά.
//        Set<SimpleGrantedAuthority> auths = resourceRoles.stream().map(role ->
//                new SimpleGrantedAuthority("ROLE_" +  role)).collect(toSet());

        Set<SimpleGrantedAuthority> auths = resourceRoles.stream().map(role ->
                new SimpleGrantedAuthority(role)).collect(toSet());

        log.info("in authorities {}",auths.toString());
        return auths;
    }
}
