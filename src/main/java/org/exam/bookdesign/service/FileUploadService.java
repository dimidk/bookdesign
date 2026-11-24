package org.exam.bookdesign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;


    public boolean isZipFile(MultipartFile file) {

        if (file == null) {
            return false;
        }

        return file.getContentType().contains("application/zip") || file.getOriginalFilename().contains(".zip");
    }


    public boolean isValidFileSize(MultipartFile file) {
        if (file == null) {
            return false;
        }

        return file.getSize() > MAX_FILE_SIZE;
    }

    public byte[] storeFileBytes(MultipartFile file) throws IOException {
        if (file == null) {
            return null;
        }

        return file.getBytes();
    }

}
