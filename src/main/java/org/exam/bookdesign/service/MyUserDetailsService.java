package org.exam.bookdesign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.model.BookUser;
import org.exam.bookdesign.repository.BookUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final BookUserRepository bookUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Optional<BookUser> bookUser = bookUserRepository.getBookUserByUsername(username);

        if (bookUser.isEmpty()) {
            log.info("in bookUserService loadUserByUsername");
            throw new UsernameNotFoundException(username);
        }
        BookUser bookUser1 = bookUser.get();

        return User.withUsername(bookUser1.getUsername())
                .password(bookUser1.getPassword())
                .authorities(bookUser1.getAuthorities())
                .roles("USER")
                .build();

    }
}
