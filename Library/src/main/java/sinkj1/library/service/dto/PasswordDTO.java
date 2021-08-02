package sinkj1.library.service.dto;

public class PasswordDTO {

    private String value;

    public PasswordDTO(){

    }

    public PasswordDTO(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PasswordDTO{" +
            "value='" + value + '\'' +
            '}';
    }
}
