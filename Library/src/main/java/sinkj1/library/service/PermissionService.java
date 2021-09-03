package sinkj1.library.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.library.domain.BaseEntity;
import sinkj1.library.security.jwt.TokenProvider;
import sinkj1.library.service.dto.DeletePermissionDto;
import sinkj1.library.service.dto.PermissionDto;

@Service
@Transactional
public class PermissionService {

    private final HttpClient<PermissionDto> httpClient;
    private final HttpClient<DeletePermissionDto> deletePermissionDtoHttpClient;
    private final TokenProvider tokenProvider;

    @Autowired
    public PermissionService(
        HttpClient<PermissionDto> httpClient,
        HttpClient<DeletePermissionDto> deletePermissionDtoHttpClient,
        TokenProvider tokenProvider
    ) {
        this.httpClient = httpClient;
        this.deletePermissionDtoHttpClient = deletePermissionDtoHttpClient;
        this.tokenProvider = tokenProvider;
    }

    public void addPermissionForUser(BaseEntity targetObj, Permission permission, String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        httpClient.post(
            "https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/permission/user",
            new PermissionDto(targetObj.getId(), targetObj.getClass().getName(), permission.getMask(), username),
            token
        );
    }

    public void addPermissionForAuthority(BaseEntity targetObj, Permission permission, String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        httpClient.post(
            "https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/permission/authority",
            new PermissionDto(targetObj.getId(), targetObj.getClass().getName(), permission.getMask(), authority),
            token
        );
    }

    public void addPermissions(List<PermissionDto> permissionDtos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        httpClient.post("https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/permissions/user", permissionDtos, token);
    }

    public void deletePermission(DeletePermissionDto deletePermissionDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        deletePermissionDtoHttpClient.post(
            "https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/delete-permission/user",
            deletePermissionDto,
            token
        );
    }
}
