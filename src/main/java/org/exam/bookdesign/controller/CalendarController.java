package org.exam.bookdesign.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.model.BookUser;
import org.exam.bookdesign.model.BookingRecord;
import org.exam.bookdesign.service.BookUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value="/auth")
public class CalendarController {

    private final BookUserService bookUserService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

     record LoginRequest(@JsonProperty("username") String username,
                         @JsonProperty("email") String email,
                         @JsonProperty("fullname") String fullname,
                         @JsonProperty("password") String password,
                         @JsonProperty("role") String role) {}
     record LoginResponse(
                          @JsonProperty("id") int id,
                          @JsonProperty("username") String username,
                          @JsonProperty("email") String email,
                          @JsonProperty("fullname") String fullname,
                          @JsonProperty("password") String password,
                          @JsonProperty("role") String role) {}

    @PostMapping(value = "/login",produces = {"application/json"})
    public LoginResponse login(HttpServletRequest req, @RequestBody LoginRequest loginRequest, HttpServletResponse resp) {
        //  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse rep) {
    //public ResponseEntity<String> login(HttpServletRequest req, @RequestBody LoginRequest loginRequest) {
        //if (bookUser == null) {


        if (loginRequest == null) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                                    loginRequest.username, loginRequest.password));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        req.getSession().setAttribute("user", authentication.getPrincipal());
//
//        log.info("see what request has {}",req.getUserPrincipal().toString());

//
//
        Optional<BookUser> test = bookUserService.findBookUserByUsername(loginRequest.username);
//
//        log.info("authentication principal {}",authentication.getPrincipal().toString());
//

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.username, loginRequest.password);

        Authentication authentication = authenticationManager.authenticate(token);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, req, resp);

        req.getSession().setAttribute("user", userDetails);

        return new LoginResponse(test.get().getBookUserId(),
                                                test.get().getUsername(),
                test.get().getEmail(),
                test.get().getFullname(),
                test.get().getPassword(),
                userDetails.getAuthorities().toString());

    }

    /**
     * have to see the returned request from existing user
     * @param bookUser
     * @return
     */

    @PostMapping(path = "/register")
    public ResponseEntity<Optional<BookUser>> register(@RequestBody BookUser bookUser) {

//        log.info("register: {}", bookUser.getEmail());
        if (bookUser.getUsername().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (bookUserService.bookUserExists(bookUser.getUsername())) {
            log.info("User {} already exists", bookUser.getUsername());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        bookUserService.addUser(bookUser);

        return new ResponseEntity<>(HttpStatus.valueOf("CREATED"));
    }


}
