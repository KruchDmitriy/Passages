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

        Edge stub = new Edge(Edge.EdgeType.HORZ, 0, 0);
        for (int i = 0; i < 2 * size * (size + 1); i++) {
            edges.add(stub);
        }

        for (int i = 0; i < size + 1; i++) {
            for (int h_j = 0; h_j < size; h_j++) {
                System.out.println("i " + i + " h_j " + h_j);
                System.out.println("idx " + (i * (2 * size + 1) + h_j));
                edges.setElementAt(new Edge(Edge.EdgeType.HORZ, i, h_j),
                        i * (2 * size + 1) + h_j);
            }
            for (int v_i = 0; v_i < size; v_i++) {
                System.out.println("v_i " + v_i + " i " + i);
                System.out.println("idx " + (v_i * (2 * size + 1) + size + i));
                edges.setElementAt(new Edge(Edge.EdgeType.VERT, v_i, i),
                        v_i * (2 * size + 1) + size + i);
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

    public Board(Board board) {
        this(board.getSize());

        for (int i = 0; i < 2 * size * (size + 1); i++) {
            edges.elementAt(i).setReservedBy(
                    board.getEdges().elementAt(i).getReservedBy());
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
            return getHorizontalEdges().get(i * size + j);
        } else {
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
