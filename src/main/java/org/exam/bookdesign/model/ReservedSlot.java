package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
//@Table(name="timeslot")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
@ToString
public class ReservedSlot {

   // @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private int slotId;

   // @Column(name="start")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime start;

   // @Column(name="date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern="dd-MM-yyyy HH:mm",shape = JsonFormat.Shape.STRING)
    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    private State status;

    public boolean overlaps(ReservedSlot reservedSlot) {

        return this.start.isBefore(reservedSlot.end) &&
                this.end.isAfter(reservedSlot.start) ||
                (this.start.isEqual(reservedSlot.start) ||
                        this.end.isEqual(reservedSlot.end));

    }

}
