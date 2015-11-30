package DataStructures;

public class Edge {
    public enum WHO { BLUE, RED, NONE }
    public enum EdgeType { VERT, HORZ }

    private EdgeType type;
    private WHO reservedBy;

    public Edge(EdgeType type) {
        reservedBy = WHO.NONE;
        this.type = type;
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
}
