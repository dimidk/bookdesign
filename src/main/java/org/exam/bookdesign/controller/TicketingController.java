package org.exam.bookdesign.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.service.TicketingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ticket")
public class TicketingController {

    private final TicketingService ticketingService;

    @PostMapping(value="/send", produces = "application/json")
    public Optional<String> sendTicket() {


        return Optional.ofNullable("ok");
    }
}
