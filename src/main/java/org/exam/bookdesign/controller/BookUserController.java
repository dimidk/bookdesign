package org.exam.bookdesign.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.AttributeOverride;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.config.ApplicationAuditorAware;
import org.exam.bookdesign.model.BookUser;
import org.exam.bookdesign.service.BookUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Slf4j
public class BookUserController {
    private final BookUserService bookUserService;


   // private  ApplicationAuditorAware auditorAware;

    record UserResponse(@JsonProperty("username") String username,
                        @JsonProperty("role") String role,
                        @JsonProperty("password") String password) {}

    @GetMapping("/user")
    public UserResponse getUser(@AuthenticationPrincipal Jwt jwt) {

//        UserDetails userDetails = (UserDetails) request.getSession().getAttribute("user");
//
//        log.info("user in session {}",request.getSession().getAttribute("user"));
//        log.info("userdetails {}" ,request.getSession().getServletContext().getAttribute("user"));
//
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = jwt.getClaimAsString("preferred_username");
        log.info("getUser: {}", username);
        if (username == null ) {
            log.info("unathorized");
            return new UserResponse(username, "unathorized", "unathorized");
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

        if (!res.isPresent()) {

            return new UserResponse(username, "unathorized", "unathorized");

        }
        log.info("getUser: {}", res);
        log.info("authenticated");
        log.info("authorities ",jwt.getClaimAsString("roles"));

        return new UserResponse(username, jwt.getSubject(), jwt.getClaimAsString("roles"));
    }
}
