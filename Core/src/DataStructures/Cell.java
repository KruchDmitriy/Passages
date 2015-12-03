package DataStructures;

import java.io.Serializable;
import java.util.Vector;

public class Cell implements Serializable {
    private Vector<Edge> edges;
    private Edge.WHO reservedBy;

    public Cell() {
        this.reservedBy = Edge.WHO.NONE;
    }

    public Cell(Vector<Edge> edges) {
        assert edges.size() == 4;
        this.edges = edges;
    }

    public Edge.WHO getReservedBy() {
        return reservedBy;
    }

    public void refresh() {
        Edge.WHO who = edges.elementAt(0).getReservedBy();
        if (who == edges.elementAt(1).getReservedBy() &&
                who == edges.elementAt(2).getReservedBy() &&
                who == edges.elementAt(3).getReservedBy()) {
            reservedBy = who;
        }
    }

    public boolean isInCell(Edge edge) {
        for (Edge e : edges) {
            if (e == edge) {
                return true;
            }
        }
        return false;
    }
}
