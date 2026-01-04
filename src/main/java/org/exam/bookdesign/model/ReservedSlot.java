package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
//@Table(name="timeslot")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
@ToString
@Slf4j
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

//        log.info("start date {} and reserved slot start_date {}", start, reservedSlot.start);
//        log.info("end date {} and reserved slot end_date {}", end, reservedSlot.end);

        long secondsStart  = this.start.toEpochSecond(ZoneOffset.UTC);
        long secondsEnd = this.end.toEpochSecond(ZoneOffset.UTC);

        return secondsStart < reservedSlot.end.toEpochSecond(ZoneOffset.UTC) &&
                secondsEnd > reservedSlot.start.toEpochSecond(ZoneOffset.UTC) ||
                (secondsStart == reservedSlot.start.toEpochSecond(ZoneOffset.UTC) ||
                secondsEnd == reservedSlot.end.toEpochSecond(ZoneOffset.UTC));

//        return this.start.isBefore(reservedSlot.end) &&
//                this.end.isAfter(reservedSlot.start) ||
//                (this.start.isEqual(reservedSlot.start) ||
//                        this.end.isEqual(reservedSlot.end));

    }

}
