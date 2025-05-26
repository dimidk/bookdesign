package org.exam.bookdesign.repository;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import org.exam.bookdesign.model.BookingRecord;
import org.exam.bookdesign.model.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRepository extends JpaRepository<Lab, Long> {

    //List<BookingRecord> getBookingRecordsByLabname(String labname);
    //List<BookingRecord> getLabByLabname(String labname);
    //List<Lab> getAll();

    Lab getLabByLabname(String labname);
    List<Lab> findAll();

}
