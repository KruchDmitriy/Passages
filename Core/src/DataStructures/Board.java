package DataStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Board {
    private int size;
    private Vector<Edge> edges;
    private Vector<Cell> cells;

    public Board(int size) {
        this.size = size;
        edges = new Vector<>(2 * size * (size + 1));
        int horz_i = 0;
        int vert_i = 0;
        while(horz_i + vert_i < 2 * size * (size + 1)) {
            for (int h_i = 0; h_i < size; horz_i++, h_i++) {
                edges.add(horz_i + vert_i, new Edge(Edge.EdgeType.HORZ));
                System.out.println(horz_i + " " + vert_i + " horz" );
            }
            for (int v_i = 0; v_i < size + 1; vert_i++, v_i++) {
                edges.add(horz_i + vert_i, new Edge(Edge.EdgeType.VERT));
                System.out.println(horz_i + " " + vert_i + " vert" );
            }
        }

        cells = new Vector<Cell>(size * size);
        for (int i = 0; i < size * size; i++) {
            Vector<Edge> edgeCell = new Vector<>(4);
            int idx = i / (2 * size + 1) * (2 * size + 1) + i % size;
            edgeCell.add(edges.elementAt(idx));
            edgeCell.add(edges.elementAt(idx + size));
            edgeCell.add(edges.elementAt(idx + size + 1));
            edgeCell.add(edges.elementAt(idx + 2 * size + 1));
            cells.add(new Cell(edgeCell));
        }
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
        if (type == Edge.EdgeType.HORZ) {
            System.out.println("getEdge " + i + " " + j);
            return getHorizontalEdges().get(i * size + j);
        } else {
            System.out.println("getEdge " + i + " " + j);
            return getVerticalEdges().get(i * (size + 1) + j);
        }
    }

    public boolean apply(BoardChange boardChange) throws IllegalStateException {
        Edge edge = getEdge(boardChange.getEdgeType(),
                boardChange.getI(), boardChange.getJ());
        if (edge.getReservedBy() != Edge.WHO.NONE) {
            throw new IllegalStateException("Edge already taken");
        }
        edge.setReservedBy(boardChange.getReservedBy());
        List<Cell> active_cells = cells.stream().
                filter(p -> p.isInCell(edge)).
                collect(Collectors.toList());
        for (Cell c: active_cells) {
            Edge.WHO cellStat = c.getReservedBy();
            c.refresh();
            if (cellStat != c.getReservedBy()) {
                return true;
            }
        }
        return false;
    }

    public Scores calcScores() {
        Scores scores = new Scores(0, 0);
        for (Cell c : cells) {
            c.refresh();
            if (c.getReservedBy() == Edge.WHO.BLUE) {
                scores.incBlueScore();
            } else if (c.getReservedBy() == Edge.WHO.RED) {
                scores.incRedScore();
            }
        }
        return scores;
    }
}
