package sinkj1.library.domain;

import org.springframework.security.acls.domain.BasePermission;

public class PermissionVM {

    private Long entityId;
    private String permission;
    private String userCredentional;

    public PermissionVM(Long entityId, String permission, String userCredentional) {
        this.entityId = entityId;
        this.permission = permission;
        this.userCredentional = userCredentional;
    }

    public PermissionVM() {
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getPermission() {
        return permission;
    }

    public String getUserCredentional() {
        return userCredentional;
    }
}
