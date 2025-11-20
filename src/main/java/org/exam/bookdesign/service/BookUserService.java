package org.exam.bookdesign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.model.BookUser;
import org.exam.bookdesign.model.Role;
import org.exam.bookdesign.repository.BookUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookUserService {

    private final BookUserRepository bookUserRepository;



    public boolean bookUserExists(String username) {
      return bookUserRepository.findBookUserByUsername(username).isPresent();

    }

    public Optional<BookUser> findBookUserByUsername(String username) {
//        Optional<BookUser> user = Optional.ofNullable(bookUserRepository.findBookUserByUsername(username).orElseThrow(
//                () -> new RuntimeException("Problem with user")));

        Optional<BookUser>  user = bookUserRepository.findBookUserByUsername(username);

        return user;
    }

    public void addUser(BookUser bookUser)  {

        BookUser user = new BookUser();
        user.setUsername(bookUser.getUsername());
        user.setEmail(bookUser.getEmail());
        user.setFullname(bookUser.getFullname());
        user.setPassword(bookUser.getPassword());
       // user.setAuthorities(bookUser.getAuthorities());
        user.setRole(bookUser.getRole());


        //user.setAuthorities((Collection<? extends GrantedAuthority>) authorities);


//        user = user;
//        user.setRole(Role.USER);
        bookUserRepository.save(user);
    }
}
