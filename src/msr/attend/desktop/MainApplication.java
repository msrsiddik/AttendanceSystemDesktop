package msr.attend.desktop;

import com.google.firebase.database.DatabaseReference;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    public static DatabaseReference REF;
    public static DatabaseReference REFERENCE;

    public static void main(String[] args) throws IOException {

        FirebaseService service = new FirebaseService();
//        service.getDb().setPersistenceEnabled(true);
        REF = service.getDb().getReference("AttendInfoInUniversity");
        REFERENCE = service.getDb().getReference("Students");
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL url = this.getClass().getClassLoader().getResource("msr/attend/desktop/mainUI.fxml");
        Parent root = loader.load(url);
        primaryStage.setResizable(false);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
