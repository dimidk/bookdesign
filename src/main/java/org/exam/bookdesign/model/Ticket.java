package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

//    @JsonProperty("Content-Type")
//    private String contentType = "application/json";

//    @JsonProperty("X-Api-Key")
//    private final String api_key = "s7O4PjFxn3UovZ1ErS+vjV9fGYpyvb8No4JW6+b+zmc=";

    @JsonProperty("projectAlias")
    private String projectAlias;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("onBehalfOfUsername")
    private String onBehalfOfUsername;

    @JsonProperty("descriptionIsHtml")
    private boolean descriptionIsHtml;
}
