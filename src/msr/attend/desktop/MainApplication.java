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

    public static void main(String[] args) throws IOException {
        QRScanner qrScanner = new QRScanner();
        qrScanner.initWebcam();

        FirebaseService service = new FirebaseService();
        REF = service.getDb().getReference("AttendInfoInUniversity");

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL url = this.getClass().getClassLoader().getResource("msr/attend/desktop/mainUI.fxml");
        Parent root = loader.load(url);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
