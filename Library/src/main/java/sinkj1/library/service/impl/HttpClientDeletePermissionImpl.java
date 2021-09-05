package sinkj1.library.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sinkj1.library.service.HttpClient;
import sinkj1.library.service.dto.AclByIdDto;
import sinkj1.library.service.dto.DeletePermissionDto;
import sinkj1.library.web.rest.AccountResource;

@Service
public class HttpClientDeletePermissionImpl implements HttpClient<DeletePermissionDto> {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Autowired
    private final ObjectMapper objectMapper;

    public HttpClientDeletePermissionImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String get(String url) {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder().uri(new URI(url)).header("X-TENANT-ID", "yuradb").GET().build();
        } catch (URISyntaxException e) {
            log.error(e.toString());
        }

        HttpResponse<String> response = null;
        try {
            response = java.net.http.HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error(e.toString());
        }

        return response.body();
    }

    @Override
    public String post(String url, DeletePermissionDto dto, String token) {
        HttpRequest request = null;
        try {
            request =
                HttpRequest
                    .newBuilder()
                    .uri(new URI(url))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("X-TENANT-ID", "yuradb")
                    .timeout(Duration.ofSeconds(1))
                    .POST(HttpRequest.BodyPublishers.ofString(dto.toString()))
                    .build();
        } catch (URISyntaxException e) {
            log.error(e.toString());
        }

        HttpResponse<String> response = null;
        try {
            response = java.net.http.HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error(e.toString());
        }

        return response.body();
    }

    @Override
    public String post(String uri, List<DeletePermissionDto> entities, String token) {
        HttpRequest request = null;
        try {
            request =
                HttpRequest
                    .newBuilder()
                    .uri(new URI(uri))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("X-TENANT-ID", "yuradb")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(entities)))
                    .timeout(Duration.ofSeconds(1))
                    .build();
        } catch (URISyntaxException | JsonProcessingException e) {
            log.error(e.toString());
        }

        HttpResponse<String> response = null;
        try {
            response = java.net.http.HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error(e.toString());
        }

        return response.body();
    }
}
