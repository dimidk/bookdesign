package org.exam.bookdesign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.model.BookingRecord;
import org.exam.bookdesign.repository.BookingRecordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingRecordService {

    private final BookingRecordRepository bookingRecordRepository;

    public void add(BookingRecord bookingRecord) {

        if (bookingRecord == null) {
            log.error("bookingRecord is null");
        }
        log.info("bookingRecord: {}", bookingRecord.toString());
        bookingRecordRepository.saveAndFlush(bookingRecord);
    }

    public int countAll() {

        return bookingRecordRepository.findAll().size();
    }


    public List<BookingRecord> findBookingRecordsByLabname(String labname) {
        return bookingRecordRepository.getBookingRecordsByLab(labname);
    }

    public void deleteBookingRecord(BookingRecord bookingRecord) {
        bookingRecordRepository.delete(bookingRecord);
    }

    public void addAll(List<BookingRecord> bookingRecords) {

        bookingRecordRepository.saveAll(bookingRecords);

    }

    public void deleteByIds(List<BookingRecord> ids) {
        bookingRecordRepository.deleteAll(ids);
    }

    public void update(BookingRecord record_old,BookingRecord record) {

        bookingRecordRepository.save(record);

    }

    public BookingRecord findBookingRecordById(int id) {

        return bookingRecordRepository.findBookingRecordByBookingRecordId(id);
    }



    public void update(BookingRecord record) {
       // bookingRecordRepository.updateBookingRecord(record);
        //bookingRecordRepository.save(record);

        //BookingRecord bookingRecord = bookingRecordRepository.findBookingRecordByBookingRecordId(record.getBookingRecordId());
        if (record == null) {
            log.error("bookingRecord is null");
            throw new IllegalArgumentException("bookingRecord is null");
        }
        log.info("bookingRecord: {}", record.toString());
        bookingRecordRepository.saveAndFlush(record);

    }
}
