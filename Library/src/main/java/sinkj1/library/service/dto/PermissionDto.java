package sinkj1.library.service.dto;

import org.springframework.security.acls.model.Sid;

import java.io.Serializable;

public class PermissionDto implements Serializable {

    private Long id;
    private String className;
    private int permission;
    private String sid;

    public PermissionDto(){

    }

    public PermissionDto(Long id, String className, int permission, String sid) {
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

    public int getPermission() {
        return permission;
    }

    public String getSid() {
        return sid;
    }
}
