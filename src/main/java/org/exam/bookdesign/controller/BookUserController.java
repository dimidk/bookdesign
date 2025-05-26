package org.exam.bookdesign.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.model.BookUser;
import org.exam.bookdesign.service.BookUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Slf4j
public class BookUserController {
    private final BookUserService bookUserService;

    record UserResponse(@JsonProperty("username") String username,
                        @JsonProperty("role") String role,
                        @JsonProperty("password") String password) {}

    @GetMapping("/user")
    public UserResponse getUser(HttpServletRequest request) {

        UserDetails userDetails = (UserDetails) request.getSession().getAttribute("user");

        log.info("user in session {}",request.getSession().getAttribute("user"));
        log.info("userdetails {}" ,request.getSession().getServletContext().getAttribute("user"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (userDetails == null || auth == null || !auth.isAuthenticated()) {
            log.info("unathorized");
            return new UserResponse(auth.getName(), "unathorized", "unathorized");
        }

        Optional<BookUser> res = bookUserService.findBookUserByUsername(userDetails.getUsername());

        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        //String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("authenticated user: {} {}", userDetails.getUsername(), authenticated);

        //set authentication true to have user that is authorized and authenticated
        request.getSession().getServletContext().setAttribute("authenticated", authenticated);
        return new UserResponse(userDetails.getUsername(),
                userDetails.getAuthorities().toString(),
                res.get().getPassword());
    }
}
