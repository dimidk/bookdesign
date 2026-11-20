package org.exam.bookdesign.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.exam.bookdesign.model.Ticket;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;


@Service
@RequiredArgsConstructor
@Slf4j
public class TicketingService {

    public void sendTicket(Ticket ticket) throws IOException, ParseException {

        String url = "https://portal.it.ntua.gr/api/v1/tickets";

        String token = "s7O4PjFxn3UovZ1ErS+vjV9fGYpyvb8No4JW6+b+zmc=";

        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = objectMapper.writeValueAsString(ticket);
        log.info("Ticketing Service: ticket is: {}",body);


        HttpPost post = new HttpPost(url);
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("User-Agent", "PostmanRuntime/7.32.3");
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("X-Api-Key", token);
        post.setHeader("Cookie", "portal_affinity=6f77114908665a0a");

        StringEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
        post.setEntity(entity);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            CloseableHttpResponse response = client.execute(post);

            log.info("response is: {}", response.toString());
            String responseBody = EntityUtils.toString(response.getEntity());

            log.info("Status: {}", response.getCode());
            System.out.println("Body: " + responseBody);
        }






//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .header("Content-Type", "application/json")
//                .header("X-Api-Key",  token)
//                .header("Cookie", "portal_affinity=6f77114908665a0a")
//                .method("POST", HttpRequest.BodyPublishers.ofString(body))
//                .build();
//
//        HttpResponse<String> response = null;
//        try {
//            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//            log.info("response status {}",response.statusCode());
//            log.info("response body is {}",response.body());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        URL urlReq = new URL("https://portal.it.ntua.gr/api/v1/tickets");
//        HttpsURLConnection conn = (HttpsURLConnection) urlReq.openConnection();
//
//        conn.setRequestMethod("POST");
//        conn.setDoOutput(true);
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Accept", "application/json");
//        conn.setRequestProperty("X-Api-Key",  token) ;
//        conn.setRequestProperty("Cookie", "portal_affinity=6f77114908665a0a");
//
//        try {
//            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//            dos.writeBytes(body);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        log.info("response status {}",conn.getResponseCode());
//
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = br.readLine()) != null) {
//                log.info(line);
//            }
//        }catch (IOException e){
//            throw new RuntimeException(e);
//        }


    }
}
