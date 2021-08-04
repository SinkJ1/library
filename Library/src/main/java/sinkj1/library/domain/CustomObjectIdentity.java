package sinkj1.library.domain;

import org.springframework.security.acls.model.ObjectIdentity;

import java.io.Serializable;

public class CustomObjectIdentity implements ObjectIdentity {
    private Serializable identifier;
    private String type;

    public CustomObjectIdentity() {
    }

    public CustomObjectIdentity(Serializable identifier, String type) {
        this.identifier = identifier;
        this.type = type;
    }

    @Override
    public Serializable getIdentifier() {
        return identifier;
    }

    @Override
    public String getType() {
        return type;
    }
}
