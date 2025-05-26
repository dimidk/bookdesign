package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name="lab")
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int lab_id;

    @Column(name="labname")
    @JsonProperty("labname")
    private String labname;

   // @OneToMany(mappedBy = "timeslot",cascade = CascadeType.ALL)
    //private List<ReservedSlot> timeslot = new ArrayList<>()

   // @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL, orphanRemoval = true)
   // private List<BookingRecord> records = new ArrayList<>();
//
//    public void addBookingRecord(BookingRecord bookingRecord) {
//
//        if (records.contains(bookingRecord)) {
//            throw new IllegalArgumentException("double record");
//
//        }
//        records.add(bookingRecord);
//    }
}
