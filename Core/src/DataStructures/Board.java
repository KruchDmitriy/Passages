package DataStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Board {
    private int size;
    private Vector<Edge> edges;

    public Board(int size) {
        this.size = size;
        edges = new Vector<>(2 * size * (size + 1));
    }

    public int getSize() {
        return size;
    }

    public Vector<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Edge> getVerticalEdges() {
        List<Edge> vertEdges = edges.stream()
                .filter(p -> p.getType() == Edge.EdgeType.VERT)
                .collect(Collectors.toList());
        return new ArrayList(vertEdges);
    }

    public ArrayList<Edge> getHorizontalEdges() {
        List<Edge> horzEdges = edges.stream()
                .filter(p -> p.getType() == Edge.EdgeType.HORZ)
                .collect(Collectors.toList());
        return new ArrayList(horzEdges);
    }

    public Edge getEdge(Edge.EdgeType type, int i, int j) {
        assert i * size + j < size * (size + 1);
        if (type == Edge.EdgeType.HORZ) {
            return getHorizontalEdges().get(i * size + j);
        } else {
            return getVerticalEdges().get(i * size + j);
        }
    }
}
