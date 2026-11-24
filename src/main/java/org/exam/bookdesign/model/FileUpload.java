package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
@ToString
@Slf4j
public class FileUpload {

    @Lob
    @Column(name="zip_file", columnDefinition="LONGBLOB")
    @JsonProperty("zipfile")
    private byte[] zipFile;

    // Store original filename
    @Column(name="original_filename")
    @JsonProperty("originalfilename")
    private String originalFileName;

    // Store file size in bytes
    @Column(name="file_size")
    @JsonProperty("filesize")
    private long fileSize;

    // Store content type
    @Column(name="content_type")
    @JsonProperty("contenttype")
    private String contentType;


}
