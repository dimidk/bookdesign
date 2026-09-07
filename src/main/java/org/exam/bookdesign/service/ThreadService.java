package org.exam.bookdesign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThreadService extends Thread {


    private final SendEmailService sendEmailService;

    public void myExecute(HashMap<String,String> params, long weeks) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                sendEmailService.sendNewMail(params.get("To"), params.get("Subject"), "Κάνατε μια επαναλαμβανόμενη κράτηση για εργαστήριο/αίθουσα για " + weeks + " εβδομάδες");
                sendEmailService.sendNewMail("testdimi_1@mail.ntua.gr", params.get("Subject"), "Έγινε επαναλαμβανόμενη κράτηση για εργαστήριο/αίθουσα για εβδομάδες "+weeks);
            }
        });

        executorService.shutdown();
    }




}
