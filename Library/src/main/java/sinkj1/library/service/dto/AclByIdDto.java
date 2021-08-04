package sinkj1.library.service.dto;

import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

import java.util.List;

public class AclByIdDto {

    private ObjectIdentity oid;

    private List<Sid> sids;

    public AclByIdDto() {
    }

    public AclByIdDto(ObjectIdentity oid, List<Sid> sids) {
        this.oid = oid;
        this.sids = sids;
    }

    public ObjectIdentity getOid() {
        return oid;
    }

    public List<Sid> getSids() {
        return sids;
    }
}
