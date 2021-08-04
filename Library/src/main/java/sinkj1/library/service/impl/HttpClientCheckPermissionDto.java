package sinkj1.library.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sinkj1.library.service.HttpClient;
import sinkj1.library.service.dto.CheckPermissionDto;
import sinkj1.library.web.rest.AccountResource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class HttpClientCheckPermissionDto implements HttpClient<CheckPermissionDto> {
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);


    @Autowired
    private final ObjectMapper objectMapper;

    public HttpClientCheckPermissionDto(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String get(String url){
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        } catch (URISyntaxException e) {
            log.error(e.toString());
        }

        HttpResponse<String> response = null;
        try {
            response = java.net.http.HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            log.error(e.toString());
        } catch (InterruptedException e) {
            log.error(e.toString());
        }

        return response.body();

    }

    @Override
    public String post(String url, CheckPermissionDto dto){

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTYyODE2MTM1MH0.psJCNp4261BPq-mB_H9gPGzwpD2pJIc_TkxI3RPrmAPntAN3brUNnr2jW9FQyfACPA64vZPQfZ27oHWbZyUOqg")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(dto)))
                .build();
        } catch (URISyntaxException | JsonProcessingException e) {
            log.error(e.toString());
        }

        HttpResponse<String> response = null;
        try {
            response = java.net.http.HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            log.error(e.toString());
        } catch (InterruptedException e) {
            log.error(e.toString());
        }

        return response.body();
    }
}
