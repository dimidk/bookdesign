package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//import java.util.List;
/*
LabUser doesn't have to be an extend of User class
This is an embeddable class to booking_record table with the fields
fistname lastname just to keep this info for user that uses the lab.
But this user doesn't have to do anything with bookuser

 */

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
//public class LabUser extends User {
public class LabUser {
    @JsonProperty("fistname")
    private String Firstname;

    @JsonProperty("lastname")
    private String Lastname;
//
//    public LabUser() {
//        super();
//        this.Firstname = "";
//        this.Lastname = "";
//    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
//    }
//
//    @Override
//    public String getPassword() {
//        return "";
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> setAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        return List.of();
//    }

}
