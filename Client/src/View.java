import DataStructures.Board;
import DataStructures.Edge;
import DataStructures.Player;
import DataStructures.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Vector;

public class View {
    private Stage stage;
    private Connection connection;

    private int width = 400;
    private int height = 275;

    private Player player;
    private BoardView boardView;

    View(Stage stage, Connection connection) {
        this.stage = stage;
        this.connection = connection;
    }

    public void start() {
        registration();
    }

    public void registration() {
        stage.setTitle("Passages");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scene_title = new Text("Welcome");
        scene_title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scene_title, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);

        btn.setOnAction(event -> connection.register(userTextField.getText()));

        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        Scene scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void chooseTheRoom(List<Room> rooms) {
        Vector<String> items = new Vector<String>();
        for (Room r : rooms) {
            items.add(r.getName());
        }
        ListView<String> list = new ListView<String>();
        list.setItems(FXCollections.observableArrayList(items));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(list);

        Scene scene = new Scene(borderPane, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void setBoard(Board board) {
        boardView.setBoard(board);
    }

    private class BoardView {
        private Board board;

        public BoardView(Board board) {
            this.board = board;
        }

        public void setBoard(Board board) {
            this.board = board;
        }

        public void draw(Stage stage, int width, int height) {
            Group root = new Group();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            Canvas canvas = new Canvas(width, height);
            root.getChildren().add(canvas);

            GraphicsContext gc = canvas.getGraphicsContext2D();

            drawBoard(gc, width, height, 10);

            Font font = Font.font("Helvetica", FontWeight.BOLD, 24);
            gc.setFont(font);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);

            stage.show();
        }

        private void drawBoard(GraphicsContext gc, int width, int height, int padd) {
            double gridSizeW = (width - 2 * padd) / board.getSize();
            double gridSizeH = (height - 2 * padd) / board.getSize();

            for (int i = 0; i < board.getSize() + 1; i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    Edge hEdge = board.getEdge(Edge.EdgeType.HORZ, i, j);
                    Edge vEdge = board.getEdge(Edge.EdgeType.VERT, i, j);

                    gc.setLineWidth(5);
                    if (hEdge.getReservedBy() == Edge.WHO.NONE) {
                        gc.setStroke(Color.GRAY);
                    } else if (hEdge.getReservedBy() == Edge.WHO.BLUE) {
                        gc.setStroke(Color.BLUE);
                    } else {
                        gc.setStroke(Color.RED);
                    }
                    gc.strokeLine(j * gridSizeW + padd, i * gridSizeH + padd,
                            (j + 1) * gridSizeW + padd, i * gridSizeH + padd);

                    if (vEdge.getReservedBy() == Edge.WHO.NONE) {
                        gc.setStroke(Color.GRAY);
                    } else if (vEdge.getReservedBy() == Edge.WHO.BLUE) {
                        gc.setStroke(Color.BLUE);
                    } else {
                        gc.setStroke(Color.RED);
                    }
                    gc.strokeLine(j * gridSizeW + padd, i * gridSizeH + padd,
                            j * gridSizeW + padd, (i + 1) * gridSizeH + padd);
                }
            }
        }
    }
}
