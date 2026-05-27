package org.exam.bookdesign.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.model.*;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@NoArgsConstructor(force = true)
@Slf4j
public class BookingService {

    private final BookingRecordService bookingRecordService;
    private static final BookingManager bookingManager = BookingManager.getInstance();
   // private final ResourcePatternResolver resourcePatternResolver;


    public List<BookingRecord> queryBookingRecordsByLab(String labname) {
        return bookingRecordService.findBookingRecordsByLabname(labname);
    }

    //create labs that are in database
    public void loadFromDB(List<Lab> allLabs) {
        bookingManager.createLabs(allLabs);
    }

    public void loadFromDB(HashMap<String,List<String>> mapRooms) {
        bookingManager.createRooms(mapRooms);
    }

    //load information records from database for a specific lab
    public void loadLab(Lab labname,List<BookingRecord> records) {
//        log.info("I am in load lab");
        bookingManager.loadingLab(labname, records);
        //bookingManager.showHashMapInfo();

    }


    public int getAllRecords() {

        return bookingRecordService.countAll();
    }

    public void newBookRecordInLab(Lab lab,BookingRecord record) {

        bookingManager.updateMap(lab, record);
    }

    public List<BookingRecord> getLabRecords(Lab lab) {
        return bookingManager.getRecords(lab);
    }

    public void deleteBookRecordInLab(Lab lab,BookingRecord record) {

        bookingManager.deleteBookingRecord(lab, record);

    }

    //find overlaps
    //public Optional<BookingRecord> existBookRecordInLab(List<BookingRecord> records,BookingRecord record,long weeks) {
    public Optional<List<ReservedSlot>> findBookRecordOverlap(BookingRecord record,long weeks) {

        List<BookingRecord> records = queryBookingRecordsByLab(record.getLab());
        List<ReservedSlot> tryToReserve = new ArrayList<>();

        for (int i = 0; i <= weeks; i++) {

            ReservedSlot cur = new ReservedSlot();
            cur.setStart(record.getTimeslot().getStart().plusWeeks(i));
            cur.setEnd(record.getTimeslot().getEnd().plusWeeks(i));

            tryToReserve.add(cur);
        }
        tryToReserve.stream().forEach(cur -> {log.info("slot that want to be reserverd {}", cur.toString());});

        List<ReservedSlot> reserved = queryBookingRecordsByLab(record.getLab())
                .stream().map(BookingRecord::getTimeslot).collect(Collectors.toList());

        reserved.stream().forEach(cur -> {log.info("slot that is reserverd {}", cur.toString());});

        List<ReservedSlot> overlapedSlots = reserved.stream().filter(slot -> {
            return tryToReserve.stream().anyMatch(r -> r.overlaps(slot));
        }).toList();

        if (!overlapedSlots.isEmpty()) {
            overlapedSlots.stream().forEach(d -> {
                log.info("conflict datetime {}", d.toString());
            });
        }

        if (overlapedSlots.isEmpty()) {

            log.info("empty list of slots and empty Optional");
            return Optional.ofNullable(overlapedSlots);
        }
        log.info(" Lab is reserved and have conflict for dates that being said from boolean");
        log.info("try to reserve dates: {}", tryToReserve.stream().toList());
        log.info("overlaping reserved slots: {}", overlapedSlots.stream().toList());

        return Optional.of(overlapedSlots);
    }

    public long findWeeksToRepeat(String start, String end, String time) {


        LocalDateTime startDate = LocalDateTime.parse(start+ "T" + time);
        LocalDateTime endDate = LocalDateTime.parse(end+ "T" + time);

        long numberOfWeeks = ChronoUnit.WEEKS.between(LocalDate.parse(start), LocalDate.parse(end));
        log.info("number of weeks to reserve the event: {}", numberOfWeeks);

        return numberOfWeeks;
    }

    public List<BookingRecord> filterBookingRecords(String labname, LocalDateTime start, int time) {

        List<BookingRecord> records = queryBookingRecordsByLab(labname);
        LocalDateTime endDate = start.plusWeeks(time);

        List<BookingRecord> filteredRecords = records.stream()
                .filter(record -> {
                    LocalDateTime currentDate = record.getTimeslot().getStart();
                    return (currentDate != null &&
                            !currentDate.isBefore(start) &&
                            !currentDate.isAfter(endDate));
                }).toList();
        return filteredRecords;
    }

    public List<BookingRecord> overlapedRecords(String labname, List<ReservedSlot> overlapSlots) {

        List<BookingRecord> records = queryBookingRecordsByLab(labname);

        return  records.stream().filter(record -> {
            ReservedSlot cur = record.getTimeslot();
            return overlapSlots.contains(cur);
        }).toList();
    }


    public void deleteRepeatedBookings(List<BookingRecord> records) {

        bookingRecordService.deleteByIds(records);
    }

    public List<BookingRecord> createNewRecords(BookingRecord record,long time) {

        List<BookingRecord> records = new ArrayList<>();

        for (int i=0;i <= Long.valueOf(time).intValue() ; i++) {

            BookingRecord newRecord = BookingRecord.builder()
                    .labUser(record.getLabUser())
                    .timeslot(ReservedSlot.builder()
                            .start(record.getTimeslot().getStart().plusWeeks(i))
                            .end(record.getTimeslot().getEnd().plusWeeks(i))
                            .status(State.Reserved)
                    .build())
                    .title(record.getTitle())
                    .bookUser(record.getBookUser())
                    .lab(record.getLab())
                    .build();

            records.add(newRecord);
        }

        return records;
    }

}
