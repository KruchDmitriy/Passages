import DataStructures.*;
import com.sun.xml.internal.fastinfoset.algorithm.UUIDEncodingAlgorithm;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Vector;

public class View {
    private Stage stage;
    private Connection connection;

    private double width = 400;
    private double height = 275;

    private Player player;
    private List<RoomInfo> rooms;
    private Room room;
    private BoardView boardView;

    View(Stage stage, Connection connection) {
        this.stage = stage;
        this.connection = connection;
        start();
    }

    public void start() {
        registration();
    }

    public void registration() {
        setWindowSize(400, 275);
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

        btn.setOnAction(event ->
        {
            UUID playerId = UUID.randomUUID();
            connection.register(userTextField.getText(), playerId);
            player = new Player(userTextField.getText(), playerId);
            try {
                this.rooms = connection.getRooms();
            } catch (Exception e) {
                e.printStackTrace();
            }
            chooseTheRoom();
        });

        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        Scene scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void chooseTheRoom() {
        Button createBtn = new Button("Create room");
        Button joinBtn = new Button("Join room");

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 5, 5, 10));
        hBox.getChildren().add(joinBtn);
        hBox.getChildren().add(createBtn);


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
        for (RoomInfo r : rooms) {
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

        joinGrid.add(list, 0, 1);

        Button okBtn = new Button("Ok");
        okBtn.setPrefSize(50, 20);
        HBox hBoxBtn = new HBox();
        hBoxBtn.setPadding(new Insets(20, 20, 30, 20));
        hBoxBtn.setAlignment(Pos.CENTER);
        hBoxBtn.getChildren().add(okBtn);

        BorderPane borderPane = new BorderPane();

        borderPane.setTop(hBox);
        borderPane.setCenter(joinGrid);
        borderPane.setBottom(hBoxBtn);


        createBtn.setOnMouseClicked(event -> borderPane.setCenter(createGrid));
        joinBtn.setOnMouseClicked(event -> {
            rooms = connection.getRooms();
            chooseTheRoom();
            borderPane.setCenter(joinGrid);
        });
        okBtn.setOnMouseClicked(event -> {
            if (borderPane.getCenter() == createGrid) {
                connection.createRoom(roomTextField.getText(),
                        Integer.valueOf(sizeTextField.getText()),
                        player.getId());

                waitForPlayer();
            } else {
                int idx = list.getSelectionModel().getSelectedIndices().get(0);
                RoomInfo r = rooms.get(idx);
                connection.joinRoom(r.getId(), player.getId());
            }
        });

        Scene scene = new Scene(borderPane, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    private void waitForPlayer() {

    }

    public void setMyTurn(boolean myTurn) {
        boardView.setMyTurn(myTurn);
    }

    public void setPlayerColor(Player.Color color) {
        player.setColor(color);
    }

    public void startGame() {
        boardView = new BoardView(room.getBoard());
        Platform.runLater(() -> boardView.draw(stage));
    }

    public void updateBoard(BoardChange boardChange) {
        room.getBoard().apply(boardChange);
        Platform.runLater(() -> boardView.draw());
    }

    public void setWindowSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    private class BoardView {
        private Stage stage;
        private Board board;
        private boolean myTurn;
        private boolean gameOver;

        public BoardView(Stage stage, Board board) {
            this.stage = stage;
            this.board = board;
            this.myTurn = false;
            this.gameOver = false;
        }

        public void setMyTurn(boolean myTurn) {
            this.myTurn = myTurn;
        }

        public void draw() {
            Group root = new Group();

            Scene scene = new Scene(root, width, height);

            BorderPane borderPane = new BorderPane();

            Canvas canvas = new Canvas(width - 200, height - 100);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            drawBoard(gc, width - 200, height - 100, 20);

            canvas.setOnMouseClicked(event -> {
                        if (myTurn) {
                            double x = event.getX();
                            double y = event.getY();
                            Edge edge = null;
                            Optional<Edge> edgesOpt = board.getEdges().stream().
                                    filter(p -> p.isNeighbour(x, y)).findFirst();
                            if (edgesOpt.isPresent()) {
                                edge = edgesOpt.get();
                            } else {
                                return;
                            }
                            BoardChange boardChange = new BoardChange(
                                    edge.getI(), edge.getJ(), edge.getType(),
                                    Edge.WHO.fromColor(player.getColor()));
                            connection.takeEdge(boardChange, room.getId());
                        }}
            );

            Font labelFont = Font.font("Calibri", FontWeight.BOLD, 24);
            Font nameFont = Font.font("Calibri", FontWeight.BOLD, 24);
            Font scoreFont = Font.font("Calibri", FontWeight.BOLD, 50);
            Font messageFont = Font.font("Calibrin", FontWeight.BOLD, 36);
            gc.setFont(nameFont);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);

            Scores scores = room.getScores();

            Text bluePlayerLabel = new Text("Blue");
            bluePlayerLabel.setFont(labelFont);
            Text bluePlayerName = new Text(room.getBluePlayer().getName());
            bluePlayerName.setFont(nameFont);
            Text blueScore = new Text(String.valueOf(scores.getBlueScore()));
            blueScore.setFont(scoreFont);

            VBox blueVBox = new VBox();
            blueVBox.setAlignment(Pos.CENTER);
            blueVBox.getChildren().add(bluePlayerLabel);
            blueVBox.getChildren().add(bluePlayerName);
            blueVBox.getChildren().add(blueScore);

            Text redPlayerLabel = new Text("Red");
            redPlayerLabel.setFont(labelFont);
            Text redPlayerName = new Text(room.getRedPlayer().getName());
            redPlayerName.setFont(nameFont);
            Text redScore = new Text(String.valueOf(scores.getRedScore()));
            redScore.setFont(scoreFont);

            VBox redVBox = new VBox();
            redVBox.setAlignment(Pos.CENTER);
            redVBox.getChildren().add(redPlayerLabel);
            redVBox.getChildren().add(redPlayerName);
            redVBox.getChildren().add(redScore);

            Text message;
            if (myTurn) {
                message = new Text("Your turn");
                if (player.getColor() == Player.Color.RED) {
                    message.setFill(Color.CORAL);
                } else {
                    message.setFill(Color.LIGHTBLUE);
                }
            } else {
                message = new Text("Opponent turn");
                if (player.getColor() == Player.Color.RED) {
                    message.setFill(Color.LIGHTBLUE);
                } else {
                    message.setFill(Color.CORAL);
                }
            }

            if (gameOver) {
                message = new Text("Game over");
                message.setFill(Color.BLACK);
            }
            message.setFont(messageFont);
            HBox messageBox = new HBox(message);
            messageBox.setAlignment(Pos.CENTER);

            Button leaveBtn = new Button("Leave room");
            HBox buttonBox = new HBox(leaveBtn);
            buttonBox.setAlignment(Pos.CENTER);

            borderPane.setCenter(canvas);
            borderPane.setLeft(blueVBox);
            borderPane.setRight(redVBox);
            borderPane.setTop(messageBox);
            borderPane.setBottom(buttonBox);

            root.getChildren().add(borderPane);
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

                    gc.setLineWidth(6);
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

            for (Cell c : board.getCells()) {
                if (c.getReservedBy() == Edge.WHO.RED) {
                    gc.setFill(Color.CORAL);
                } else if (c.getReservedBy() == Edge.WHO.BLUE) {
                    gc.setFill(Color.LIGHTBLUE);
                } else {
                    continue;
                }

                double x = c.getUpLeftCorner().getX() + 5;
                double y = c.getUpLeftCorner().getY() + 5;
                double w = c.getBottomRightCorner().getX() - x - 5;
                double h = c.getBottomRightCorner().getY() - y - 5;
                System.out.println(x + " " + y + " " + w + " " + h);
                gc.fillRect(x, y, w, h);
            }
        }

        public void setGameOver(boolean gameOver) {
            this.gameOver = gameOver;
        }
    }

    public void gameOver() {
        boardView.setGameOver(true);
    }
}
