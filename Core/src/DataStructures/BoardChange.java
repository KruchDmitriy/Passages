package DataStructures;

import java.io.Serializable;

public class BoardChange implements Serializable {
    private int i;
    private int j;
    private Edge.EdgeType edgeType;
    private Edge.WHO reservedBy;

    public BoardChange(int i, int j, Edge.EdgeType edgeType, Edge.WHO reservedBy) {
        this.i = i;
        this.j = j;
        this.edgeType = edgeType;
        this.reservedBy = reservedBy;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Edge.EdgeType getEdgeType() {
        return edgeType;
    }

    public Edge.WHO getReservedBy() {
        return reservedBy;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public void setEdge(Edge.EdgeType edgeType) {
        this.edgeType = edgeType;
    }

    public void setReservedBy(Edge.WHO reservedBy) {
        this.reservedBy = reservedBy;
    }
}
