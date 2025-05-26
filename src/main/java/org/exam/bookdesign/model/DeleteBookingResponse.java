package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record DeleteBookingResponse(
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
) {
}
