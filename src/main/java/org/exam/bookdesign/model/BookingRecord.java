package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name="booking_record")
@ToString
public class BookingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int bookingRecordId;

    //@ManyToOne(cascade = CascadeType.ALL)
    @Column(name="bookusername")
    @JsonProperty("bookusername")
    private String bookUser;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name="labname")
    @Column(name="labname")
    @JsonProperty("labname")
    private String lab;

    @Embedded
    private ReservedSlot timeslot;

    @Column(name="title")
    private String title;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name="labuser")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "Firstname",column = @Column(name="firstname")),
            @AttributeOverride(name="Lastname",column = @Column(name="lastname"))
    })
    private LabUser labUser;

//    Comparable<BookingRecord> compareTo(Comparable<BookingRecord> comparable) {
//
//        comparable.
//
//    }

}
