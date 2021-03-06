package sinkj1.library.service.dto;

public class DeletePermissionDto {

    private int entityId;

    private String entityClassName;

    private int permission;

    private String user;

    public DeletePermissionDto() {}

    public DeletePermissionDto(int entityId, String entityClassName, int permission, String user) {
        this.entityId = entityId;
        this.entityClassName = entityClassName;
        this.permission = permission;
        this.user = user;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    @Override
    public String toString() {
        return (
            "DeletePermissionDto{" +
            "entityId=" +
            entityId +
            ", entityClassName='" +
            entityClassName +
            '\'' +
            ", permission=" +
            permission +
            ", user='" +
            user +
            '\'' +
            '}'
        );
    }
}
