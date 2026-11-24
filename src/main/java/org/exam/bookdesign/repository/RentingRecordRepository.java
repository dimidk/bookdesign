package org.exam.bookdesign.repository;

import org.exam.bookdesign.model.LabUser;
import org.exam.bookdesign.model.RentingRecord;
import org.exam.bookdesign.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RentingRecordRepository extends JpaRepository<RentingRecord, Long> {

//    List<RentingRecord> findAll();
//
//    RentingRecord findByRentingRecordId(Long rentingRecordId);
//
//    List<RentingRecord> findRentingRecordsByRentAuthorContains(LabUser rentAuthor);
//
//    List<RentingRecord> findRentingRecordsByRentUserContains(LabUser rentUser);
//
//    List<RentingRecord> findRentingRecordsByPriceGreaterThanEqual(int price);
//
//    List<RentingRecord> findRentingRecordsByPriceEquals(int price);
//    List<RentingRecord> findRentingRecordsByPriceLessThanEqual(int price);
//
//    List<RentingRecord> findRentingRecordsByTimeslot_Status(State status);



}
