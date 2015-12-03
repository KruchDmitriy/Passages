import DataStructures.*;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class View {
    private Stage stage;
    private Connection connection;

    private double width = 400;
    private double height = 275;

    private Player player;
    private Room room;
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
        btn.setDefaultButton(true);
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
        Button createBtn = new Button("Create room");
        Button joinBtn = new Button("Join room");

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 5, 5, 10));
        hBox.getChildren().add(createBtn);
        hBox.getChildren().add(joinBtn);


        GridPane createGrid = new GridPane();
        createGrid.setAlignment(Pos.CENTER);
        createGrid.setHgap(10);
        createGrid.setVgap(10);

        Text createTitle = new Text("Create room");
        createTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        createGrid.add(createTitle, 0, 0, 2, 1);

        Label roomName = new Label("Room name:");
        createGrid.add(roomName, 0, 1);

        TextField roomTextField = new TextField();
        createGrid.add(roomTextField, 1, 1);

        Label boardSize = new Label("Board size:");
        createGrid.add(boardSize, 0, 2);

        TextField sizeTextField = new TextField();
        createGrid.add(sizeTextField, 1, 2);


        Vector<String> items = new Vector<>();
        for (Room r : rooms) {
            if (r.isFree())
                items.add(r.getName());
        }

        GridPane joinGrid = new GridPane();
        joinGrid.setAlignment(Pos.CENTER);
        joinGrid.setHgap(10);
        joinGrid.setVgap(10);
        joinGrid.setPadding(new Insets(20, 20, 10, 10));

        Text joinTitle = new Text("Join room");
        joinTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        joinGrid.add(joinTitle, 0, 0, 2, 1);

        ListView<String> list = new ListView<>();
        list.setItems(FXCollections.observableArrayList(items));

        list.setOnMouseClicked(event ->
                System.out.println("clicked on " +
                        list.getSelectionModel().getSelectedItem()));
        joinGrid.add(list, 0, 1);

        Button okBtn = new Button("Ok");
        okBtn.setPrefSize(50, 20);
        HBox hBoxBtn = new HBox();
        hBoxBtn.setPadding(new Insets(20, 20, 30, 20));
        hBoxBtn.setAlignment(Pos.CENTER);
        hBoxBtn.getChildren().add(okBtn);

        BorderPane borderPane = new BorderPane();

        borderPane.setTop(hBox);
        borderPane.setCenter(createGrid);
        borderPane.setBottom(hBoxBtn);


        createBtn.setOnMouseClicked(event -> borderPane.setCenter(createGrid));
        joinBtn.setOnMouseClicked(event -> borderPane.setCenter(joinGrid));
        okBtn.setOnMouseClicked(event -> {
            if (borderPane.getCenter() == createGrid) {
                connection.createRoom(roomTextField.getText(),
                        Integer.valueOf(sizeTextField.getText()),
                        player.getId());
            } else {
                String str = list.getSelectionModel().getSelectedItem();
                Room r = rooms.stream().filter(p -> p.getName() == str)
                        .findFirst().get();
                connection.joinRoom(r.getId(), player.getId());
            }
        });

        Scene scene = new Scene(borderPane, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void startGame() {
        boardView = new BoardView(room.getBoard());
        boardView.draw(stage);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void updateBoard(BoardChange boardChange) {
        room.getBoard().apply(boardChange);
        boardView.draw(stage);
    }

    public void setWindowSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    private class BoardView {
        private Board board;

        public BoardView(Board board) {
            this.board = board;
        }

        public void draw(Stage stage) {
            Group root = new Group();
            Scene scene = new Scene(root, width, height);

            Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
            root.getChildren().add(canvas);

            GraphicsContext gc = canvas.getGraphicsContext2D();

            drawBoard(gc, width, height, 20);

            scene.setOnMouseClicked(event -> {
                double x = event.getX();
                double y = event.getY();
                Edge edge = board.getEdges().stream().
                        filter(p -> p.isNeighbour(x, y)).findFirst().get();
                BoardChange boardChange = new BoardChange(
                        edge.getI(), edge.getJ(), edge.getType(),
                        Edge.WHO.fromColor(player.getColor()));
                connection.takeEdge(boardChange, room.getId());
            }
            );


            Font font = Font.font("Helvetica", FontWeight.BOLD, 24);
            gc.setFont(font);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);

            stage.setScene(scene);
            stage.show();
        }

        private void drawBoard(GraphicsContext gc,
                               double width, double height, double padd) {
            double gridSizeW = (width - 2 * padd) / board.getSize();
            double gridSizeH = (height - 2 * padd) / board.getSize();

            for (int i = 0; i < board.getSize() + 1; i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    Edge hEdge = board.getEdge(Edge.EdgeType.HORZ, i, j);
                    Edge vEdge = board.getEdge(Edge.EdgeType.VERT, j, i);

                    gc.setLineWidth(4);
                    if (hEdge.getReservedBy() == Edge.WHO.NONE) {
                        gc.setStroke(Color.GRAY);
                    } else if (hEdge.getReservedBy() == Edge.WHO.BLUE) {
                        gc.setStroke(Color.BLUE);
                    } else {
                        gc.setStroke(Color.RED);
                    }
                    gc.strokeLine(j * gridSizeW + padd, i * gridSizeH + padd,
                            (j + 1) * gridSizeW + padd, i * gridSizeH + padd);
                    hEdge.setP1(new javafx.geometry.Point2D(
                            j * gridSizeW + padd,
                            i * gridSizeH + padd));
                    hEdge.setP2(new javafx.geometry.Point2D(
                            (j + 1) * gridSizeW + padd,
                            i * gridSizeH + padd));


                    if (vEdge.getReservedBy() == Edge.WHO.NONE) {
                        gc.setStroke(Color.GRAY);
                    } else if (vEdge.getReservedBy() == Edge.WHO.BLUE) {
                        gc.setStroke(Color.BLUE);
                    } else {
                        gc.setStroke(Color.RED);
                    }
                    gc.strokeLine(i * gridSizeW + padd, j * gridSizeH + padd,
                            i * gridSizeW + padd, (j + 1) * gridSizeH + padd);
                    vEdge.setP1(new javafx.geometry.Point2D(
                            i * gridSizeW + padd,
                            j * gridSizeH + padd));
                    vEdge.setP2(new javafx.geometry.Point2D(
                            i * gridSizeW + padd,
                            (j + 1) * gridSizeH + padd));
                }
            }
        }
    }

    public void gameOver() {

    }
}
