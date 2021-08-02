package sinkj1.library.service;

import sinkj1.library.service.dto.PasswordDTO;

public interface HttpClient {

    String get(String url);
    String post(String url, PasswordDTO dto);

}
