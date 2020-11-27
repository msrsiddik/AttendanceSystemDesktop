package msr.attend.desktop;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import msr.attend.desktop.model.AttendModel;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static msr.attend.desktop.MainApplication.REF;

public class MainUIController implements QRScanner.VirtualCardScanData {

    private long ldt = Calendar.getInstance().getTime().getTime();
    private DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @FXML
    private TextArea logTextArea;

    @FXML
    private Label scanMsgLbl;

    static String msg = "Machine Ready";

    @FXML
    void initialize() throws IOException {
        File file = new File(sdf.format(new Date(ldt))+".txt");
        if (!file.exists()){
            file.createNewFile();
        }
        readLogFile();
        scanMsgLbl.setText(msg);

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
        AttendModel model = new AttendModel(s, "Siddik", "42", ldt);

        String date = sdf.format(new Date(ldt));

        currentlyPresent(s, date, new AttendCurrentStatus() {
            @Override
            public void statusInListener() {
                REF.child("IN").child(date).child(String.valueOf(ldt))
                        .setValue(model, (databaseError, databaseReference) -> {
                            System.out.println("success");
                            REF.child("CurrentStatus").child(date).child(s).setValueAsync("in");
                            logWrite(date, s,Calendar.getInstance().getTime(),"in");
                        });
            }

            @Override
            public void statusOutListener() {
                REF.child("OUT").child(date).child(String.valueOf(ldt))
                        .setValue(model, (databaseError, databaseReference) -> {
                            System.out.println("success");
                            REF.child("CurrentStatus").child(date).child(s).setValueAsync("out");
                            logWrite(date, s,Calendar.getInstance().getTime(),"out");
                        });
            }
        });

    }

    private void logWrite(String fileName, String s, Date time, String status) {
        try{
            FileWriter writer = new FileWriter(fileName+".txt",true);
            writer.write(s+" | "+time+" | "+status+"\n");
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
