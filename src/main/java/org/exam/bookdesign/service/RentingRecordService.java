package org.exam.bookdesign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exam.bookdesign.repository.RentingRecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentingRecordService {

    public final RentingRecordRepository rentingRecordRepository;




}
