package msr.attend.desktop;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseService {
    FirebaseDatabase db;

    public FirebaseService() throws IOException {

        FileInputStream serviceAccount =
                new FileInputStream("src\\resource\\key.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://attendance-system-905aa.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        db = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase getDb() {
        return db;
    }
}
