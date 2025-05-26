package org.exam.bookdesign.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.repository.LabRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the way for singleton instance to be used among the controllers
 */

@Getter
@Setter
@Slf4j

public class BookingManager {

    @Getter
    private HashMap<Lab ,List<BookingRecord>> labs ;
    private static  BookingManager bookingManager ;


    private BookingManager() {

        this.labs = new HashMap<>();
    }

    public static BookingManager getInstance() {

        if (bookingManager == null)
            bookingManager = new BookingManager();
        else
            throw new RuntimeException("BookingManager has already been initialized");

        return bookingManager;
    }


    //create labs in HashMap
    public void createLabs(List<Lab> labNames) {

//        List<Lab> labsFromDb = labRepository.findAll();
//
//        for (Lab lab : labsFromDb) {
//
//            lab.setLabname(lab.getLabname());
//            lab.setLab_id(lab.getLab_id());
//            List<BookingRecord> records = new ArrayList<>();
//
//            this.labs.put(lab, records);
//
//        }

        for (Lab lab : labNames) {

            lab.setLabname(lab.getLabname());
            lab.setLab_id(lab.getLab_id());
            List<BookingRecord> records = labs.get(lab);
            if (records == null) {
                records = new ArrayList<>();
            }

            this.labs.put(lab, records);
        }

    }

    //load all labs info from database to hashmap
    public void loadingLab(Lab labName, List<BookingRecord> records) {

        log.info("Lab information for lab: {}",labName);

        if (this.labs.containsKey(labName) && this.labs.get(labName).size() > 0) {

            this.labs.get(labName).clear();
            this.labs.get(labName).addAll(records);
        }

        this.labs.put(labName, records);
//        for (Map.Entry<Lab,List<BookingRecord>> lab : this.labs.entrySet()) {
//
//            log.info("Lab name: {}",lab.getKey().getLabname());
//            log.info("Lab id: {}",lab.getKey().getLab_id());
//            lab.getValue().forEach(record -> {log.info("booking record for lab: {}",record);});
//
//        }
    }

    //update hashmap with new booking record to lab
    public void updateMap(Lab lab,BookingRecord bookingRecord) {

        List<BookingRecord> records = this.labs.get(lab);
        if (records == null) {
            throw new RuntimeException("BookingRecord is null");
        }
        records.add(bookingRecord);
        this.labs.put(lab, records);

    }
    //delete booking record from lab in hashmap
    public void deleteBookingRecord(Lab lab,BookingRecord bookingRecord) {

        List<BookingRecord> records = this.labs.get(lab);
        if (records == null) {
            throw new RuntimeException("BookingRecord is null");
        }
        records.remove(bookingRecord);
        this.labs.put(lab, records);
    }

    //update booking record in lab and update hashmap
    public void updateBookingRecord(BookingRecord bookingRecord, Lab lab) {}

    //find booking record in lab hashmap
    public boolean findBookingRecord(Lab lab, BookingRecord bookingRecord) {
        return false;
    }

    public List<BookingRecord> getRecords(Lab lab) {
        return this.labs.get(lab);
    }


    public void showLabInfo(Lab lab) {

        log.info("Lab information for lab: {}",lab.getLabname());

        List<BookingRecord> records = labs.get(lab);
        records.forEach(record -> {log.info("booking record for lab: {}",record);});
    }

    public void showHashMapInfo() {

        for (Map.Entry<Lab,List<BookingRecord>> labListEntry : this.labs.entrySet()) {

            Lab lab = labListEntry.getKey();
            log.info("Lab information for lab: {}", lab.getLabname());
            if (labListEntry.getValue().isEmpty() || labListEntry.getValue() == null) {

                log.info("No records found for lab: {}", lab.getLabname());
            } else {
                labListEntry.getValue().forEach(record -> {
                    log.info("booking record for lab: {}", record);
                });
            }
        }
    }

}
