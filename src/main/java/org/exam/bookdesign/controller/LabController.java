package org.exam.bookdesign.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.model.BookingRecord;
import org.exam.bookdesign.model.Lab;
import org.exam.bookdesign.repository.LabRepository;
import org.exam.bookdesign.service.BookingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
public class LabController {

    private final BookingService bookingService;
    private final LabRepository labRepository;

//    private void loadLabs(List<String> labs) {
//
//        int i = 0;
//        for (String lab : labs) {
//
//            List<BookingRecord> bookingRecords = new ArrayList<>();
//            bookingRecords = bookingService.queryBookingRecordsByLab(lab);
//            bookingRecords.forEach(bookingRecord -> {log.info(bookingRecord.toString());});
//
//            Lab labTemp = BookingManager.getInstance().createLabs(labs.get(i),i);
//            BookingManager.getInstance().addLab(labTemp,bookingRecords);
//            i++;
//        }
//    }
//
//    public HashMap<String, Lab> gettingLabs(){
//        return BookingManager.getInstance().getLabs();
//    }

    @GetMapping("/labs")
    public List<String> getLabs() {
        List<String> labnames = new ArrayList<>();

        HashMap<String,List<String>> mapRooms = new HashMap<>();

        List<Lab> labs = labRepository.findAll();
        //labRepository.findAll().forEach(lab -> {labs.put(lab.getLabname(),lab);});

        List<String> listLabs = labs.stream().filter(l -> l.getType().equals("Εργαστήριο")).map(Lab::getLabname).collect(Collectors.toList());
        List<String> listClassrooms = labs.stream().filter(l-> l.getType().equals("Αίθουσα")).map(Lab::getLabname).collect(Collectors.toList());
        mapRooms.put("Εργαστήριο",listLabs);
        mapRooms.put("Αίθουσα",listClassrooms);


        labs.forEach(lab -> labnames.add(lab.getLabname()));

        bookingService.loadFromDB(labs);
        bookingService.loadFromDB(mapRooms);
//must return labname and type to process with classrooms reservation
        //loadLabs(labnames);
        return labnames;

    }


    record BookingRecordResp(
            @JsonProperty("id") int id,
            @JsonProperty("bookusername") String bookuser,
            @JsonProperty("labuser") String labuser,
            @JsonProperty("title") String title,
            @JsonProperty("start") @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)LocalDateTime start,
            @JsonProperty("end")
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING) LocalDateTime end,
            @JsonProperty("labname") String labname
    ){}

    //loads calendar info from database
    @GetMapping(value = "/labs/{labname}",produces = {"application/json"})
    public List<BookingRecordResp> getLabBookingRecords(@PathVariable String labname) {

        log.info("events request in {}",labname);
        Lab lab = labRepository.getLabByLabname(labname);

        List<BookingRecord> bookingRecords = new ArrayList<>();
        bookingRecords = bookingService.queryBookingRecordsByLab(labname);

        bookingService.loadLab(lab,bookingRecords);

        List<BookingRecordResp> bookings = new ArrayList<>();
        for (BookingRecord b : bookingRecords) {
            BookingRecordResp resp = new BookingRecordResp(b.getBookingRecordId(),
                    b.getBookUser(),
                    b.getLabUser().getFirstname(),
                    b.getTitle(),
                    b.getTimeslot().getStart(), b.getTimeslot().getEnd(),
                    b.getLab());
            bookings.add(resp);
        }

//        Lab labTemp = labRepository.getLabByLabname(labname);
//        List<BookingRecord> list = BookingManager.getInstance().getLabs().get(labTemp);
//        list.forEach(b -> { log.info(b.toString());});

        return bookings;

        //return labRepository.getBookingRecordsByLabname(labname);
    }




}
