package DataStructures;

import javafx.geometry.Point2D;

public class Edge {
    public enum WHO {
        BLUE, RED, NONE;
        public static WHO fromColor(Player.Color color) {
            if (color == Player.Color.RED) {
                return RED;
            }
            return BLUE;
        }
    }
    public enum EdgeType { VERT, HORZ }

    private Point2D p1, p2;
    private int i, j;

    private EdgeType type;
    private WHO reservedBy;

    public Edge(EdgeType type, int i, int j) {
        reservedBy = WHO.NONE;
        this.type = type;
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public EdgeType getType() {
        return type;
    }

    public WHO getReservedBy() {
        return reservedBy;
    }

    public void setType(EdgeType type) {
        this.type = type;
    }

    public void setReservedBy(WHO reservedBy) {
        this.reservedBy = reservedBy;
    }

    public boolean isNeighbour(double x, double y) {
        if (type == EdgeType.HORZ) {
            if (x > p1.getX() && x < p2.getX() &&
                    y < p1.getY() + 5 && y > p1.getY() - 5)
                return true;
        } else {
            if (y > p1.getY() && y < p2.getY() &&
                    x < p1.getX() + 5 && x > p1.getX() - 5)
                return true;
        }
        return false;
    }

    public void setP1(Point2D p1) {
        this.p1 = p1;
    }

    public void setP2(Point2D p2) {
        this.p2 = p2;
    }

    public Point2D getP1() {
        return p1;
    }

    public Point2D getP2() {
        return p2;
    }
}
