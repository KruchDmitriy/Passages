import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Connection connection = new Connection(Connection.ConnectionType.RELEASE);
        View view = new View(primaryStage, connection);
        Client client = new Client(view);
        connection.setClient(client);
    }
}