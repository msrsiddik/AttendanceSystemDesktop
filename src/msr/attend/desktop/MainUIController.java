package msr.attend.desktop;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import msr.attend.desktop.model.AttendModel;
import msr.attend.desktop.model.StudentModel;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static msr.attend.desktop.MainApplication.REF;
import static msr.attend.desktop.MainApplication.REFERENCE;

public class MainUIController implements QRScanner.VirtualCardScanData {

    private long ldt = Calendar.getInstance().getTime().getTime();
    private DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

    @FXML
    private TextArea logTextArea;

    @FXML
    private Label scanMsgLbl;

    @FXML
    private ImageView camera;

    static String msg = "Machine Ready";

    @FXML
    void initialize() throws IOException {
        File file = new File(sdf.format(new Date(ldt))+".txt");
        if (!file.exists()){
            file.createNewFile();
        }
        readLogFile();
        scanMsgLbl.setText(msg);

        QRScanner qrScanner = new QRScanner();
        qrScanner.initWebcam(camera, 0);

    }

    @FXML
    void detailsWindow(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("timeTracking.fxml"));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Stay Time Details");
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e){
            System.out.println("Not Load Second Window");
            System.out.println(e.getMessage());
        }
    }

    private void readLogFile() {
        Thread thread = new Thread(() -> {
            try {
                FileInputStream stream = new FileInputStream(sdf.format(new Date(ldt))+".txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                String line;

                while (true) {
                    line = bufferedReader.readLine();
                    if (line == null) {
                        Thread.sleep(100);
                    } else {
                        logTextArea.appendText(line+"\n");
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @Override
    public void getUIDListener(String uid) {
        if (uid.length() == 20 && uid.startsWith("-")) {
            System.out.println(uid);
            insertAttendData(uid);
        } else {
            System.out.println("Wrong id card");
        }
    }

    private void insertAttendData(String s) {
        AttendModel model = new AttendModel();

            REFERENCE.orderByChild("id").equalTo(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        StudentModel studentModel = ds.getValue(StudentModel.class);
                        if (studentModel.getId().equals(s)) {
                            model.setUID(studentModel.getId());
                            model.setRoll(studentModel.getRoll());
                            model.setName(studentModel.getName());
                            model.setBatch(studentModel.getBatch());
                            model.setDepart(studentModel.getDepartment());
                            model.setDateTime(ldt);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        String date = sdf.format(new Date(ldt));

        currentlyPresent(s, date, new AttendCurrentStatus() {
            @Override
            public void statusInListener() {
                REF.child("IN").child(date).child(String.valueOf(ldt))
                        .setValue(model, (databaseError, databaseReference) -> {
                            System.out.println("success");
                            REF.child("CurrentStatus").child(date).child(s).setValueAsync("in");
                            logWrite(date, model.getRoll()+" - "+model.getName()+" - "+model.getBatch()+" - "+model.getDepart(),timeFormat.format(Calendar.getInstance().getTime()),"in");
                        });
            }

            @Override
            public void statusOutListener() {
                REF.child("OUT").child(date).child(String.valueOf(ldt))
                        .setValue(model, (databaseError, databaseReference) -> {
                            System.out.println("success");
                            REF.child("CurrentStatus").child(date).child(s).setValueAsync("out");
                            logWrite(date, model.getRoll()+" - "+model.getName()+" - "+model.getBatch()+" - "+model.getDepart(),timeFormat.format(Calendar.getInstance().getTime()),"out");
                        });
            }
        });

    }

    private void logWrite(String fileName, String s, String time, String status) {
        try{
            FileWriter writer = new FileWriter(fileName+".txt",true);
            writer.write(s+" - "+time+" - "+status+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void currentlyPresent(String id, String date, AttendCurrentStatus currentStatus) {
        List<String> ids = new ArrayList<>();
        REF.child("CurrentStatus").child(date).orderByChild(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ids.clear();
                        if (!dataSnapshot.exists()) {
                            currentStatus.statusInListener();
                        }
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            String getId = d.getKey();
                            ids.add(getId);
                            String getValue = d.getValue(String.class);
                            if (getId.equals(id) && getValue.equals("in")) {
                                currentStatus.statusOutListener();
                            } else if (getId.equals(id) && getValue.equals("out")) {
                                currentStatus.statusInListener();
                            }
                        }
                        if (!ids.contains(id)) {
                            currentStatus.statusInListener();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    interface AttendCurrentStatus {
        void statusInListener();

        void statusOutListener();
    }

}
