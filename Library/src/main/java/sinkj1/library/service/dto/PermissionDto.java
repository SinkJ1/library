package sinkj1.library.service.dto;

import org.springframework.security.acls.model.Sid;

import java.io.Serializable;

public class PermissionDto implements Serializable {

    private Long id;
    private String className;
    private String permission;
    private String sid;

    public PermissionDto(){

    }

    public PermissionDto(Long id, String className, String permission, String sid) {
        this.id = id;
        this.className = className;
        this.permission = permission;
        this.sid = sid;
    }

    public Long getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getPermission() {
        return permission;
    }

    public String getSid() {
        return sid;
    }
}
