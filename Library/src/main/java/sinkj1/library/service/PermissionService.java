package sinkj1.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.library.domain.BaseEntity;
import sinkj1.library.security.jwt.TokenProvider;
import sinkj1.library.service.dto.PermissionDto;

@Service
@Transactional
public class PermissionService {

    private final HttpClient<PermissionDto> httpClient;
    private final TokenProvider tokenProvider;

    @Autowired
    public PermissionService(HttpClient<PermissionDto> httpClient, TokenProvider tokenProvider) {
        this.httpClient = httpClient;
        this.tokenProvider = tokenProvider;
    }

    public void addPermissionForUser(BaseEntity targetObj, Permission permission, String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        String token = tokenProvider.createToken(authentication, false);
        httpClient.post("http://localhost:8085/api/permission/user",new PermissionDto(targetObj.getId(), targetObj.getClass().getName(),permission.getMask(), username), token);
    }
    public void addPermissionForAuthority(BaseEntity targetObj, Permission permission, String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        httpClient.post("http://localhost:8085/api/permission/authority",new PermissionDto(targetObj.getId(), targetObj.getClass().getName(),permission.getMask(), authority), token);
    }

}
