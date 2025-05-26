package org.exam.bookdesign.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class OldBookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int oldBookingId;

    @ManyToOne()
    @JoinColumn(name="username")
    private BookUser bookUser;

    @ManyToOne()
    @JoinColumn(name="labname")
    private Lab labor;
}
