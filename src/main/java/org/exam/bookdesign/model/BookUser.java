package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
//@NoArgsConstructor
//@Data
@Builder
@Getter
@Setter
@Table(name="bookuser")
@Entity
public class BookUser extends User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int bookUserId ;


    @Enumerated(EnumType.STRING)
    @JsonProperty("role")
    private Role role;

   public BookUser() {
       super();
   }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
       return super.getUsername();
    }


    @Override
    public String getPassword() {
        return "nobody";
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

}
