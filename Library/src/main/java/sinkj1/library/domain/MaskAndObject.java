package sinkj1.library.domain;

import java.io.Serializable;

public class MaskAndObject implements Serializable {

    private Long objId;
    private int mask;

    public MaskAndObject(Long objId, int mask) {
        this.objId = objId;
        this.mask = mask;
    }

    public MaskAndObject() {}

    public Long getObjId() {
        return objId;
    }

    public int getMask() {
        return mask;
    }

    @Override
    public String toString() {
        return "MaskAndObject{" +
            "objId=" + objId +
            ", mask=" + mask +
            '}';
    }
}
