package sinkj1.library.service.impl;

import com.sun.mail.imap.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.model.Acl;
import org.springframework.stereotype.Service;
import sinkj1.library.service.HttpClient;
import sinkj1.library.service.dto.AclByIdDto;
import sinkj1.library.service.dto.PermissionDto;
import sinkj1.library.web.rest.AccountResource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class HttpClientImplAcl implements HttpClient<AclByIdDto> {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Override
    public String get(String url){
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
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
    public String post(String url, AclByIdDto dto, String header){
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + header)
                .POST(HttpRequest.BodyPublishers.ofString(dto.toString()))
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

}
