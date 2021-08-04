package sinkj1.library.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.Authentication;
import sinkj1.library.domain.CustomObjectIdentity;

import java.io.Serializable;

public class CheckPermissionDto implements Serializable {

    private CustomObjectIdentity customObjectIdentity;
    private Object permission;

    public CheckPermissionDto(){

    }

    public CheckPermissionDto(CustomObjectIdentity customObjectIdentity, Object permission) {
        this.customObjectIdentity = customObjectIdentity;
        this.permission = permission;
    }

    public CustomObjectIdentity getCustomObjectIdentity() {
        return customObjectIdentity;
    }

    public Object getPermission() {
        return permission;
    }
}
