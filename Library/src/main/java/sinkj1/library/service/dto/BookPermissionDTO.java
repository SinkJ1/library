package sinkj1.library.service.dto;

import java.io.Serializable;

public class BookPermissionDTO implements Serializable {

    private Long id;
    private String name;
    private String mask;

    public BookPermissionDTO() {}

    public BookPermissionDTO(Long id, String mask) {
        this.id = id;
        this.mask = mask;
    }

    public BookPermissionDTO(Long id, String name, String mask) {
        this.id = id;
        this.name = name;
        this.mask = mask;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    @Override
    public String toString() {
        return "BookPermissionDTO{" + "id=" + id + ", name='" + name + '\'' + ", mask='" + mask + '\'' + '}';
    }
}
