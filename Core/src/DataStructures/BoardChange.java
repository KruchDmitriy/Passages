package DataStructures;

import java.io.Serializable;

/**
 * Created by VladVin on 17.10.2015.
 */
public class BoardChange implements Serializable {
    public enum Edge {LEFT, TOP};
    private int i;
    private int j;
    private Edge edge;

    public BoardChange(int i, int j, Edge edge) {
        this.i = i;
        this.j = j;
        this.edge = edge;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }
}
