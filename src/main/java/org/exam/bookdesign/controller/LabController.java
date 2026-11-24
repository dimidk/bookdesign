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
import java.util.*;
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

// Change 1: 28.9.2026 Getting and number of seats for each lab

    record LabRecordResp(
            @JsonProperty("labname") String labname,
            @JsonProperty("seats") int seats
    ){}
    @GetMapping("/labs")
//    public List<String> getLabs() {
    public List<LabRecordResp> getLabs() {

        List<LabRecordResp> labinfo = new ArrayList<>();
        List<Lab> labs = labRepository.findAll();
//        labs.forEach(lab -> labnames.add(lab.getLabname()));

        labs.forEach(lab -> {
            LabRecordResp resp = new LabRecordResp(lab.getLabname(), lab.getSeats());
            labinfo.add(resp);
        });

        bookingService.loadFromDB(labs);
//must return labname and type to process with classrooms reservation
        //loadLabs(labnames);
        //return labnames;
        return labinfo;

    }

    record RoomTypeRecordResp(
            @JsonProperty("id") int lab_id,
            @JsonProperty("labname") String labname,
            @JsonProperty("type") String type,
            @JsonProperty("tm") String tm,
            @JsonProperty("seats") int seats
    ){}

    @GetMapping(value = "/roomDescription/{roomType}/{selectedRoom}", produces = {"application/json"})
    public Optional<RoomTypeRecordResp> describeRoom(@PathVariable String roomType, @PathVariable String selectedRoom) {

        HashMap<String,List<Lab>> mapRooms = new HashMap<>();

        List<Lab> rooms = labRepository.findAll();
        //List<Lab> roomsType = rooms.stream().filter(l -> l.getType().equals(roomType)).toList();

        List<Lab> selectedLab = rooms.stream().filter(lab -> lab.getType().equals(roomType)).filter(lab -> lab.getLabname().equals(selectedRoom)).toList();
        log.info(selectedLab.toString());

        if (selectedLab.isEmpty()) {
            return Optional.empty();
        }
        RoomTypeRecordResp resp = new RoomTypeRecordResp(
                selectedLab.get(0).getLab_id(),
                selectedLab.get(0).getLabname(),
                selectedLab.get(0).getType(),
                selectedLab.get(0).getTm(),
                selectedLab.get(0).getSeats()
        );

        return Optional.of(resp);
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
