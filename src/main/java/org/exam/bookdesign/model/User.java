package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

//declare this class as mappedsuper class not to be confused with entity table
//@AllArgsConstructor
//@NoArgsConstructor
@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements UserDetails {


    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("password")
    private String password;



    public abstract Collection<? extends GrantedAuthority> getAuthorities();
    public abstract Collection<? extends GrantedAuthority> setAuthorities(Collection<? extends GrantedAuthority> authorities);
}
