package org.exam.bookdesign.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.config.KeycloakJwtAuthenticationConverter;
import org.exam.bookdesign.model.*;
import org.exam.bookdesign.repository.BookingRecordRepository;
import org.exam.bookdesign.repository.LabRepository;
import org.exam.bookdesign.repository.RentingRecordRepository;
import org.exam.bookdesign.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rent")
@RequiredArgsConstructor
@Slf4j
public class RentingRecordController {


    private final RentingRecordRepository rentingRecordRepository;
    private final LabRepository labRepository;
    private final RentingRecordService rentingRecordService;
    private final BookUserService bookUserService;
    private final SendEmailService sendEmailService;
    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;


    record RentingRecordRequest(
//            @JsonProperty("id") int id,
            @JsonProperty("rentuser") String rentUser,
            @JsonProperty("labclassname") String labClassName,
            @JsonProperty("title") String title,
            @JsonProperty("start")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING) LocalDateTime start,
            @JsonProperty("end")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING) LocalDateTime end,
            @JsonProperty("rentAuthor") String rentAuthor,
            @JsonProperty("price") int price
    ) {}

    record RentingRecordRespId(
            @JsonProperty("id") int id,
            @JsonProperty("rentuser") String rentUser,
            @JsonProperty("labclassname") String labClassName,
            @JsonProperty("title") String title,
            @JsonProperty("start")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING) LocalDateTime start,
            @JsonProperty("end")
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING) LocalDateTime end,
            @JsonProperty("rentAuthor") String rentAuthor,
            @JsonProperty("price") int price
    ) {}


    @PostMapping(value="/newrenting", produces = {"application/json"})
    public Optional<RentingRecordRespId> newRenting(@RequestBody BookingRecordController.BookingRecordRequest bookingRecordReq,
                                                                            @AuthenticationPrincipal Jwt jwt) {
        //public String newBooking(@RequestBody BookingRecord bookingRecord) {

        //if (bookingRecord == null) {
        return Optional.ofNullable(null);

    }

    @PostMapping("/{id}/upload")
    public HttpStatus uploadFiles(@PathVariable int id, @RequestParam("files") MultipartFile files) {


        return HttpStatus.OK;
    }

}
