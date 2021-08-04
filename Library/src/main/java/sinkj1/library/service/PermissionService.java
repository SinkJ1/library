package sinkj1.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.library.domain.BaseEntity;
import sinkj1.library.service.dto.PermissionDto;

@Service
@Transactional
public class PermissionService {


    private PlatformTransactionManager transactionManager;

    private final HttpClient<PermissionDto> httpClient;

    @Autowired
    public PermissionService(PlatformTransactionManager transactionManager, HttpClient<PermissionDto> httpClient) {
        this.transactionManager = transactionManager;
        this.httpClient = httpClient;
    }

    public void addPermissionForUser(BaseEntity targetObj, Permission permission, String username) {
        final Sid sid = new PrincipalSid(username);
        addPermissionForSid(targetObj, permission, sid);
    }
    public void addPermissionForAuthority(BaseEntity targetObj, Permission permission, String authority) {
        final Sid sid = new GrantedAuthoritySid(authority);
        addPermissionForSid(targetObj, permission, sid);
    }

    private void addPermissionForSid(BaseEntity targetObj, Permission permission, Sid sid) {
        httpClient.post("http://localhost:8085/api/permission/authority",new PermissionDto(targetObj.getId(), targetObj.getClass().getName(),"READ", "user"), "");
    }


}
