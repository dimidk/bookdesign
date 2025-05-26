package org.exam.bookdesign.repository;

import org.exam.bookdesign.model.BookUser;
import org.exam.bookdesign.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookUserRepository extends JpaRepository<BookUser, Long> {




    Optional<BookUser> findBookUserByUsername(String username);
    //boolean existsByUsername(String username);

    Optional<BookUser> getBookUserByUsername(String username);





    //Optional<BookUser> getBookUserByUsername(String username);
    BookUser findByEmail(String email);

}
