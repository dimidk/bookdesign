package org.exam.bookdesign.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.AttributeOverride;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.config.KeycloakJwtAuthenticationConverter;
import org.exam.bookdesign.model.*;
import org.exam.bookdesign.repository.BookingRecordRepository;
import org.exam.bookdesign.repository.LabRepository;
import org.exam.bookdesign.service.BookUserService;
import org.exam.bookdesign.service.BookingRecordService;
import org.exam.bookdesign.service.BookingService;
import org.exam.bookdesign.service.SendEmailService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class BookingRecordController {

    private final BookingRecordRepository bookingRecordRepository;
    private final LabRepository labRepository;
    private final BookingRecordService bookingRecordService;
    private final BookingService bookingService;
    private final BookUserService bookUserService;
    private final SendEmailService sendEmailService;

    private String userRole;

    record BookingRecordRequest(
            @JsonProperty("id") int id,
            @JsonProperty("bookusername") String bookuser,
            @JsonProperty("labname") String labname,
            @JsonProperty("title") String title,
            @JsonProperty("start")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING) LocalDateTime start,
            @JsonProperty("end")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING) LocalDateTime end,
            @JsonProperty("labusername") String labuser
    ) {}
    record BookingRecordResponse(
            @JsonProperty("bookusername") String bookuser,
            @JsonProperty("labname") String labname,
            @JsonProperty("title") String title,
            @JsonProperty("start")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING) LocalDateTime start,
            @JsonProperty("end")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING) LocalDateTime end,
            @JsonProperty("labusername") String labuser
    ) {}

    record BookingRecordRespId(
            @JsonProperty("id") int id,
            @JsonProperty("bookusername") String bookuser,
            @JsonProperty("labname") String labname,
            @JsonProperty("title") String title,
            @JsonProperty("start")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING) LocalDateTime start,
            @JsonProperty("end")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING) LocalDateTime end,
            @JsonProperty("labusername") String labuser
    ) {}

   // @PreAuthorize("USER")
    @GetMapping(value = "/all",produces = {"application/json"})
    public String getAllBookingRecords() {

        List<BookingRecord> bookingRecords = bookingRecordRepository.findAll();


        List<Integer> sortById = bookingRecords.stream()
                .map(BookingRecord::getBookingRecordId).sorted().toList();

        return String.valueOf(sortById.get(sortById.size()-1));
    }


//    private String getUserRole(@AuthenticationPrincipal Jwt jwt) {
//
//        Map<String,Object> roles = jwt.getClaim("book-client");
//        Collection<Object> rolenames = roles.values();
//
//
//        return "role";
//    }

    @PostMapping(value = "/findBooking", produces = {"application/json"})
    public Optional<BookingRecordRespId> getBookingRecords(@RequestBody BookingRecordRequest bookingRecordReq) {

//        return bookingRecordRepository.getBookingRecordsByLab_Labname(labname);



        if (bookingRecordReq == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        log.info("record details {} {} {} {} {}",
                bookingRecordReq.bookuser, bookingRecordReq.labname, bookingRecordReq.start,
                bookingRecordReq.end, bookingRecordReq.labuser);

        List<BookingRecord> records = bookingService.filterBookingRecords(bookingRecordReq.labname, bookingRecordReq.start, 0);

        if (records.isEmpty()) {
            return Optional.empty();
        }
        if (records.size() > 1)
            log.info("there are many records, expecting one");

        return Optional.of(new BookingRecordRespId(
                records.get(0).getBookingRecordId(),
                records.get(0).getBookUser(),
                records.get(0).getLab(),
                records.get(0).getTitle(),
                records.get(0).getTimeslot().getStart(),
                records.get(0).getTimeslot().getEnd(),
                records.get(0).getLabUser().getFirstname()
        ));
    }

    @PostMapping(value="/newbooking", produces = {"application/json"})
    public Optional<BookingRecordRespId> newBooking(@RequestBody BookingRecordRequest bookingRecordReq,@AuthenticationPrincipal Jwt jwt) {
    //public String newBooking(@RequestBody BookingRecord bookingRecord) {

        //if (bookingRecord == null) {
        if (bookingRecordReq == null) {
            log.info("bookingRecord is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        log.info("bookingRecordReq: {}", bookingRecordReq.toString());
        Lab lab = labRepository.getLabByLabname(bookingRecordReq.labname);
        log.info("Lab : {} {}", lab.getLab_id(),lab.getLabname());
        log.info("bookingRecordRequest start and end time: {} {} {}", bookingRecordReq.id,bookingRecordReq.start,bookingRecordReq.end);

//        log.info("displaying jwt token claims {}", getUserRole(jwt));

        log.info("getting user role {}", jwt.getClaimAsString("authorities"));

        BookingRecord bookingRecord = BookingRecord.builder()
                //.bookingRecordId(bookingRecordReq.id)
                .bookUser(bookingRecordReq.bookuser)
                .lab(bookingRecordReq.labname)
                .title(bookingRecordReq.title)
                .timeslot(ReservedSlot.builder()
                        .start(bookingRecordReq.start)
                        .end(bookingRecordReq.end)
                        .status(State.Reserved).build())
                .labUser(LabUser.builder()
                        .Firstname(bookingRecordReq.labuser)
                        .Lastname(bookingRecordReq.labuser)
                        .build())
                .build();

        log.info("bookingRecord created from request is {}", bookingRecord.toString());

        Optional<List<ReservedSlot>> result = bookingService.findBookRecordOverlap(bookingRecord,0);
        if (result.isPresent() && !result.get().isEmpty()) {
            log.info("Booking record cannot be added! There is overlap with the booking record");

            BookingRecordRespId res = null;
            return Optional.ofNullable(res);
        }

        bookingRecordService.add(bookingRecord);
        log.info("booking record added in db");

        return Optional.of(new BookingRecordRespId(
                bookingRecord.getBookingRecordId(),
                bookingRecordReq.bookuser,
                bookingRecordReq.labname,bookingRecordReq.title,bookingRecordReq.start,
                bookingRecordReq.end,bookingRecordReq.labuser));

    }

    @PostMapping(value="/repeat_booking/{start}/{end}")
    public Optional<Map<Integer,List<BookingRecordRespId>>> repeatNewBooking(@RequestBody BookingRecordRequest bookingRecordReq,
                                                            @PathVariable String start,
                                                            @PathVariable String end) {

        Map<Integer,List<BookingRecordRespId>> resResp = new HashMap<>();

        //get time of repeated event
        String time = bookingRecordReq.start.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        long weeks = bookingService.findWeeksToRepeat(start,end,time);

        List<BookingRecord> newRecords = new ArrayList<>();

        BookingRecord newRecord =  BookingRecord.builder()
                .bookUser(bookingRecordReq.bookuser)
                .lab(bookingRecordReq.labname)
                .title(bookingRecordReq.title)
                .labUser(LabUser.builder()
                .Firstname(bookingRecordReq.labuser)
                        .Lastname(bookingRecordReq.labuser)
                        .build())
                .timeslot(ReservedSlot.builder()
                        .start(bookingRecordReq.start)
                        .end(bookingRecordReq.end)
                        .status(State.Reserved)
                        .build())
                .build();

        Optional<List<ReservedSlot>> result = bookingService.findBookRecordOverlap(newRecord,weeks);
        List<BookingRecord> overlapRecords = bookingService.overlapedRecords(bookingRecordReq.labname,result.orElseThrow());
        log.info("overlap records {}",overlapRecords.stream().toList());

        List<BookingRecordRespId> bookingRecordRespIds = new ArrayList<>();
        if (!result.get().isEmpty()) {
            log.info("time slot is reserved already in db");
            bookingRecordRespIds = createResponseList(overlapRecords);
            resResp.put(0,bookingRecordRespIds);

            return Optional.of(resResp);
        }

        newRecords = bookingService.createNewRecords(newRecord,weeks);
        bookingRecordService.addAll(newRecords);

        log.info("booking record added in db");
        log.info("booking responses {}",bookingRecordRespIds.stream().toList());

        bookingRecordRespIds = createResponseList(newRecords);
        resResp.put(1,bookingRecordRespIds);
        return Optional.of(resResp);
    }


    record DeleteBookingRequest(
                                @JsonProperty("id") int id,
                                @JsonProperty("bookusername") String bookuser,
                                @JsonProperty("labname") String labname,
                                @JsonProperty("title") String title,
                                @JsonProperty("start")
                                @JsonFormat(pattern="yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
                                LocalDateTime start,
                                @JsonProperty("end")
                                //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
                                @JsonFormat(pattern="yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
                                LocalDateTime end,
                                @JsonProperty("labusername") String labusername
                                ) {}

//    record DeleteBookingResp(
//            @JsonProperty("id") int id,
//            @JsonProperty("bookusername") String bookuser,
//            @JsonProperty("labname") String labname,
//            @JsonProperty("title") String title,
//            @JsonProperty("start")
//            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING) LocalDateTime start,
//            @JsonProperty("end")
//            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING) LocalDateTime end,
//            @JsonProperty("labusername") String labuser
//    ) {}

    @PostMapping(value = "/delete",produces = {"application/json"})
    public  DeleteBookingResponse deleteBookingRecord(@RequestBody DeleteBookingRequest deleteReq, @AuthenticationPrincipal Jwt jwt) {

        if (deleteReq == null) {
            log.info("bookingRecord is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        log.info("bookingRecordReq: {}", deleteReq.toString());

        Optional<BookingRecord> result = existDeleteRecordInLab(deleteReq.labname,deleteReq);
        if (result.isEmpty()) {
            log.info("bookingRecord is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        BookingRecord bookingRecord = result.get();
        log.info("the booking record to delete {}",bookingRecord.getBookingRecordId());
        String username = jwt.getClaimAsString("preferred_username");
        String role = jwt.getClaimAsString("authorities");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();

        log.info("username logged in: {} has role {}", username,role);
        if (!username.equals(deleteReq.bookuser) && !role.contains("ROLE_ADMIN")) {
            log.info("Not authorized to delete record booked by {}",deleteReq.bookuser);
        }
        else if (bookingRecord.getBookingRecordId() != deleteReq.id){
            log.info("Try to delete other booking record");


        }
        else {
            bookingRecordService.deleteBookingRecord(bookingRecord);
            log.info("delete request {}", bookingRecord.toString());

            HashMap<String,String> params = prepareEmailParams(username,bookingRecord);
//           sendEmail.emailParams(params);
            sendEmailService.sendNewMail(params.get("To"), params.get("Subject"), params.get("Body"));
            //stop annoying Maria
//            String to_sec = "mkyrieri@central.ntua.gr";
//            sendEmailService.sendNewMail(to_sec, params.get("Subject"), params.get("Body"));


        }
        
        return new DeleteBookingResponse(bookingRecord.getBookingRecordId(),
                bookingRecord.getBookUser(),
                bookingRecord.getTitle(),
                bookingRecord.getLab(),
                bookingRecord.getTimeslot().getStart(),
                bookingRecord.getTimeslot().getEnd(), bookingRecord.getLabUser().getFirstname());
    }

    @PostMapping(value="/repeat_deleting/{start}/{end}")
    public Optional<DeleteBookingResponse> repeatDeleteBooking(@RequestBody DeleteBookingRequest deleteReq,
                                                           @PathVariable String start,
                                                           @PathVariable String end)
    {

        if (deleteReq == null) {
            log.info("bookingRecord is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        log.info("bookingRecordReq: {}", deleteReq.toString());


        String time = deleteReq.start.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        long weeks = bookingService.findWeeksToRepeat(start,end,time);

        Optional<BookingRecord> result = existDeleteRecordInLab(deleteReq.labname,deleteReq);
        if (result.isEmpty()) {
            log.info("bookingRecord is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        BookingRecord bookingRecord = result.get();

        if (!bookingRecord.getBookUser().equals(deleteReq.bookuser)) {
            log.info("Not authorized to delete booking record");
        }
        else {
            List<BookingRecord> deletionRecords = bookingService.filterBookingRecords(deleteReq.labname, deleteReq.start, Integer.parseInt(String.valueOf(weeks)));
            if (deletionRecords.isEmpty()) {
                log.info("There are no records to delete");
                return Optional.empty();
            }
            bookingService.deleteRepeatedBookings(deletionRecords);
            log.info("delete request {}", deleteReq.toString());
        }

        return Optional.of(new DeleteBookingResponse(bookingRecord.getBookingRecordId(),
                bookingRecord.getBookUser(),
                bookingRecord.getTitle(),
                bookingRecord.getLab(),
                bookingRecord.getTimeslot().getStart(),
                bookingRecord.getTimeslot().getEnd(),
                bookingRecord.getLabUser().getFirstname()));

    }


    @PostMapping(value="/updateTitle/{id}")
    public ResponseEntity<HttpStatus> updateEvent(@RequestBody BookingRecordRequest bookingRecordReq, @PathVariable int id) {

        BookingRecord recordToUpdate = bookingRecordService.findBookingRecordById(id);

        BookingRecord recordUpdatedData = BookingRecord.builder()
                .bookingRecordId(bookingRecordReq.id)
                .bookUser(bookingRecordReq.bookuser)
                .lab(bookingRecordReq.labname)
                .labUser(LabUser.builder()
                        .Lastname(bookingRecordReq.labuser)
                        .Firstname(bookingRecordReq.labuser)
                        .build())
                .title(bookingRecordReq.title)
                .timeslot(ReservedSlot.builder()
                        .start(bookingRecordReq.start)
                        .end(bookingRecordReq.end)
                        .status(State.Reserved)
                        .build())
                .build();

        if (recordToUpdate.getTitle().equals(recordUpdatedData.getTitle())) {

            log.info("title is the same!");
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        bookingRecordService.update(recordUpdatedData);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping(value="/updateDate/{id}")
    public ResponseEntity<HttpStatus> updateEventDate(@RequestBody BookingRecordRequest bookingRecordReq, @PathVariable int id) {

        BookingRecord recordToUpdate = bookingRecordService.findBookingRecordById(id);

        BookingRecord recordUpdatedData = BookingRecord.builder()
                .bookingRecordId(bookingRecordReq.id)
                .bookUser(bookingRecordReq.bookuser)
                .lab(bookingRecordReq.labname)
                .title(bookingRecordReq.title)
                .labUser(LabUser.builder()
                        .Lastname(bookingRecordReq.labuser)
                        .Firstname(bookingRecordReq.labuser)
                        .build())
                .timeslot(ReservedSlot.builder()
                        .start(bookingRecordReq.start)
                        .end(bookingRecordReq.end)
                        .status(State.Reserved)
                        .build())
                .build();

        Optional<List<ReservedSlot>> result = bookingService.findBookRecordOverlap(recordUpdatedData,0);
        if (!result.get().isEmpty()) {

            log.info("Booking record update can't be done because of overlap");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        bookingRecordService.update(recordUpdatedData);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(value="/updateTime/{id}")
    public ResponseEntity<HttpStatus> updateTimeSlot(@RequestBody BookingRecordRequest bookingRecordReq, @PathVariable int id) {

        if (id <= -1 || bookingRecordReq == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        BookingRecord recordToUpdate = bookingRecordService.findBookingRecordById(id);

        if (recordToUpdate.getBookingRecordId() != bookingRecordReq.id) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        recordToUpdate.getTimeslot().setEnd(bookingRecordReq.end);
        bookingRecordService.update(recordToUpdate);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private List<BookingRecordRespId> createResponseList(List<BookingRecord> records) {

        List<BookingRecordRespId> bookingRecordRespIds = new ArrayList<>();
        records.forEach(record -> {
            bookingRecordRespIds.add(new BookingRecordRespId(
                    record.getBookingRecordId(),
                    record.getBookUser(),
                    record.getLab(),
                    record.getTitle(),
                    record.getTimeslot().getStart(),
                    record.getTimeslot().getEnd(),
                    record.getLabUser().getFirstname()
            ));
        });
        return bookingRecordRespIds;
    }

    private Optional<BookingRecord> existDeleteRecordInLab(String labname,DeleteBookingRequest deleteReq) {

        List<BookingRecord> records = bookingRecordService.findBookingRecordsByLabname(labname);

        records.stream().forEach(dbRecord -> {log.info("dbRecord: {}", dbRecord.toString());});
        Optional<BookingRecord> result = records.stream().filter(r ->
                (r.getTimeslot().getStart().equals(deleteReq.start)
                        && r.getTimeslot().getEnd().equals(deleteReq.end))).findFirst();
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        BookingRecord bookingRecord = result.get();
        if (!records.contains(bookingRecord)) {
            return Optional.empty();
        }

        return Optional.of(bookingRecord);
    }

    private HashMap<String,String> prepareEmailParams(String username, BookingRecord record) {

        HashMap<String,String> params = new HashMap<>();

        Optional<BookUser> userOptional = bookUserService.findBookUserByUsername(username);
        BookUser user = userOptional.get();
        String timeStart = record.getTimeslot().getStart().toString().split("T")[1];
        String timeEnd = record.getTimeslot().getEnd().toString().split("T")[1];
        String dateStart = record.getTimeslot().getStart().toString().split("T")[0];

//        String to  = user.getEmail();
        String to = "dimideka.dimi@gmail.com";

        params.put("To",to);

        String subject = "Days and Times of reservation Lab changed!";
        params.put("Subject",subject);

        String body = "Dear user " + user.getFullname() + ",\n\n";
        body = body + "Τα διατμηματικά εργαστήρια είναι ελεύθερα για κάποιες μέρες και ώρες μετά από ακυρώσεις.  \n";
        body = body + "Συγκεκριμένα το εργαστήριο " + record.getLab() + " την ημερομηνία " + dateStart + " " + timeStart +"-" + timeEnd + "\n";
        body = body + "Παρακαλώ ελέγξτε την περίπτωση αν επιθυμείτε να κάνετε κράτηση αυτές τις μέρες.  \n\n";
        body = body + "Με εκτίμηση \n\n" + "Central of National Technical University of Athens";

        params.put("Body",body);
        return params;
    }
}
