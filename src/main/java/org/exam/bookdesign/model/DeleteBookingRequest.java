package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record DeleteBookingRequest(
        @JsonProperty("bookusername") String bookuser,
        @JsonProperty("labname") String labname,
        @JsonProperty("title") String title,
        @JsonProperty("start")
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
        LocalDateTime start,
        @JsonProperty("end")
        //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
        LocalDateTime end
) {
}
