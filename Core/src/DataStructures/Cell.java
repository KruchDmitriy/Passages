package DataStructures;

import java.io.Serializable;
import java.util.Vector;
import javafx.geometry.Point2D;

public class Cell implements Serializable {
    private Vector<Edge> edges;
    private Edge.WHO reservedBy;

    public Cell() {
        this.reservedBy = Edge.WHO.NONE;
    }

    public Cell(Vector<Edge> edges) {
        assert edges.size() == 4;
        this.edges = edges;
        reservedBy = Edge.WHO.NONE;
    }

    public Edge.WHO getReservedBy() {
        return reservedBy;
    }

    public void refresh(Edge.WHO whoLast) {
        for (Edge edge : edges) {
            if (edge.getReservedBy() == Edge.WHO.NONE) {
                return;
            }
        }
        reservedBy = whoLast;
    }

    public boolean isInCell(Edge edge) {
        for (Edge e : edges) {
            if (e == edge) {
                return true;
            }
        }
        return false;
    }

    public Point2D getUpLeftCorner() {
        return edges.elementAt(0).getP1();
    }

    public Point2D getBottomRightCorner() {
        return edges.elementAt(3).getP2();
    }
}
