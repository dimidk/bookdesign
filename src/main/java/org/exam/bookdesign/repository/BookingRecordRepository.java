package org.exam.bookdesign.repository;


import org.exam.bookdesign.model.BookUser;
import org.exam.bookdesign.model.BookingRecord;
import org.exam.bookdesign.model.ReservedSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRecordRepository extends JpaRepository<BookingRecord, Long> {

    List<BookingRecord> findAll();

    @Override
    void deleteAll(Iterable<? extends BookingRecord> entities);

    List<BookingRecord> getBookingRecordsByLab(String lab);

    BookingRecord findBookingRecordByTimeslot_StartAndTimeslot_End(LocalDateTime start, LocalDateTime end);

    BookingRecord findBookingRecordByBookingRecordId(int id);

 //   void updateBookingRecord(BookingRecord bookingRecord);



}
