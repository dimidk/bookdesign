package org.exam.bookdesign.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.AttributeOverride;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.config.KeycloakJwtAuthenticationConverter;
import org.exam.bookdesign.model.BookUser;
import org.exam.bookdesign.model.User;
import org.exam.bookdesign.model.Role;
import org.exam.bookdesign.service.BookUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


/***
 *έβαλα στο keycloak στο register να κάνει assign to ROLE USER και να
 * γίνεται έτσι εγγραφή στο βάση. Αυτό όμως δεν το θέλω όταν κάνω login με sso
 * και δεν μπορώ να κάνω register εκεί, γιατί είναι στο production keycloak.
 * έτσι όποιος χρήστης θέλει να κάνει κράτηση θα πρέπει να δηλώνεται και να γίνεται
 * add από τον administrator.
 */



@RestController
@RequiredArgsConstructor
@Slf4j
public class BookUserController {
    private final BookUserService bookUserService;
    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

    record UserResponse(@JsonProperty("username") String username,
                        @JsonProperty("role") String role,
                        @JsonProperty("password") String password,
                        @JsonProperty("userEmail") String email) {}

    @GetMapping("/user")
    public UserResponse getUser(@AuthenticationPrincipal Jwt jwt) {

        String userRole = "";

//        Map<String, Object> typeClaims =  jwt.getClaims();
//        typeClaims.keySet().stream().forEach(k -> log.info("{}: {}", k, typeClaims.get(k)));
        List<GrantedAuthority> authorities = keycloakJwtAuthenticationConverter.convert(jwt).getAuthorities().stream()
                .filter(s -> s.getAuthority().contains("ADMIN")).toList();
                        //.equals("USER")).toList();
        if (authorities.isEmpty()) {
            userRole = "USER";
        }
        else {
            userRole = authorities.get(0).getAuthority();
        }


        String username = jwt.getClaimAsString("preferred_username");
        log.info("bookUserController from jwt: {} {}", username,userRole);

        if (username == null ) {
            log.info("unathorized");
            return new UserResponse(username, "unathorized", "unathorized","");
        }
//
//        Optional<BookUser> res = bookUserService.findBookUserByUsername(userDetails.getUsername());
//
//        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
//        //String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("authenticated user: {} {}", userDetails.getUsername(), authenticated);
//
//        //set authentication true to have user that is authorized and authenticated
//        request.getSession().getServletContext().setAttribute("authenticated", authenticated);
//        return new UserResponse(userDetails.getUsername(),
//                userDetails.getAuthorities().toString(),
//                res.get().getPassword());

        //δεν επιστρέφει καλά τους ρόλους γιατί δεν είναι ακόμα σωστά configured αλλά γενικά βρίσκει το χρήστη κι είναι ok
        Optional<BookUser> res = bookUserService.findBookUserByUsername(username);


        if (res.isEmpty()) {
            List<String> roles = jwt.getClaimAsStringList("authorities");

            BookUser user = BookUser.builder()
                    .build();

            user.setUsername(username);
            user.setEmail(jwt.getClaimAsString("email"));
            user.setFullname(jwt.getClaimAsString("name"));
            user.setRole(Role.valueOf(userRole));

            bookUserService.addUser(user);

            return  new UserResponse(username, userRole, jwt.getSubject(),user.getEmail());

        }
        log.info("getUser: {}", res);
        log.info("authenticated");

        log.info("roles {} username {}",userRole,username);
        return new UserResponse(username, userRole, jwt.getSubject(),res.get().getEmail());
    }
}
