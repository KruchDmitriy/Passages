package DataStructures;

import java.io.Serializable;

/**
 * Created by VladVin on 17.10.2015.
 */
public class Cell implements Serializable {
    public enum WHOS {PBLUE, PRED, NONE}
    private WHOS left;
    private WHOS top;
    private WHOS reservedBy;

    public Cell() {
        this.left = WHOS.NONE;
        this.top = WHOS.NONE;
        this.reservedBy = WHOS.NONE;
    }

    public WHOS getLeft() {
        return left;
    }

    public WHOS getTop() {
        return top;
    }

    public WHOS getReservedBy() {
        return reservedBy;
    }

    public void setLeft(WHOS left) {
        this.left = left;
    }

    public void setTop(WHOS top) {
        this.top = top;
    }

    public void setReservedBy(WHOS reservedBy) {
        this.reservedBy = reservedBy;
    }
}
