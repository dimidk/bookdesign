package org.exam.bookdesign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="renting_record")
@Getter
@Setter
@ToString
public class RentingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int rentingRecordId;

    //@ManyToOne(cascade = CascadeType.ALL)
    @Column(name="rentuser")
    @JsonProperty("rentuser")
    private String rentUser;

    // Store the ZIP file as binary data in database
//    @Lob
//    @Column(name="zip_file", columnDefinition="LONGBLOB")
//    @JsonProperty("zipfile")
//    private byte[] zipFile;
//
//    // Store original filename
//    @Column(name="original_filename")
//    @JsonProperty("originalfilename")
//    private String originalFileName;
//
//    // Store file size in bytes
//    @Column(name="file_size")
//    @JsonProperty("filesize")
//    private long fileSize;
//
//    // Store content type
//    @Column(name="content_type")
//    @JsonProperty("contenttype")
//    private String contentType;

    @Embedded
    private FileUpload fileUpload;

    @Column(name="labclassname")
    @JsonProperty("labclassname")
    private String labClassName;

    @Embedded
    private ReservedSlot timeslot;

    @Column(name="title")
    @JsonProperty("title")
    private String title;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="Lastname",column = @Column(name="rentAuthor"))
    })
    private LabUser rentAuthor;

    @Column(name="price")
    @JsonProperty("price")
    private int price;



}
