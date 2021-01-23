package msr.attend.desktop;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import msr.attend.desktop.model.ReportModel;
import msr.attend.desktop.model.StayTimePerStudent;
import msr.attend.desktop.model.Student;

import java.io.*;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TimeTracking {

    @FXML
    private TableView<StayTimePerStudent> tableView;

    @FXML
    private TableColumn<String, ?> departCol;

    @FXML
    private TableColumn<String, ?> batchCol;

    @FXML
    private TableColumn<String, ?> rollCol;

    @FXML
    private TableColumn<String, ?> nameCol;

    @FXML
    private TableColumn<String, ?> stayTimeCol;


    @FXML
    private ChoiceBox<String> dateChoiceBox;

    List<StayTimePerStudent> stayTimePerStudents = new ArrayList<>();

    @FXML
    void initialize() {

        File file = new File("C://Users/msrsi/IdeaProjects/AttendanceSystem/");
        List<String> list = new ArrayList<>();
        for (String f : file.list()) {
            if (f.contains(".txt")){
                list.add(f);
            }
        }

        dateChoiceBox.getItems().addAll(list);
        dateChoiceBox.getSelectionModel().selectLast();

        dateChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                try {
                    stayTimePerStudents.clear();
                    tableView.getItems().clear();
                    tableDataSetUp(dateChoiceBox.getSelectionModel().getSelectedItem());
                    tableView.getItems().addAll(stayTimePerStudents);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            tableDataSetUp(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime())+".txt");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tableView.getItems().addAll(stayTimePerStudents);

        departCol.setCellValueFactory(new PropertyValueFactory<>("depart"));
        batchCol.setCellValueFactory(new PropertyValueFactory<>("batch"));
        rollCol.setCellValueFactory(new PropertyValueFactory<>("roll"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        stayTimeCol.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
    }

    private void tableDataSetUp(String dateFile) throws IOException, InterruptedException {
        FileInputStream inputStream = new FileInputStream(dateFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        Deque<ReportModel> reportModels = new ArrayDeque<>();
        while (reader.read()!=-1){
            line = reader.readLine();
            if (line == null){
                Thread.sleep(100);
            } else {
                String[] arr = line.split(" - ");
                ReportModel model = new ReportModel(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
                reportModels.add(model);
            }
        }

        Map<Student, Deque<String>> studentInTimeMap = new HashMap<>();
        Map<Student, Deque<String>> studentOutTimeMap = new HashMap<>();
        reportModels.forEach(reportModel -> {
            Student student = new Student(reportModel.getName(),reportModel.getRoll(),reportModel.getBatch(), reportModel.getDepart());
            if (reportModel.getStatus().equals("in")) {
                if (studentInTimeMap.containsKey(student)) {
                    Deque<String> deque = studentInTimeMap.get(student);
                    deque.add(reportModel.getDateTime());
                } else {
                    studentInTimeMap.put(student,new ArrayDeque<String>(Collections.singleton(reportModel.getDateTime())));
                }
            } else if (reportModel.getStatus().equals("out")){
                if (studentOutTimeMap.containsKey(student)) {
                    Deque<String> deque = studentOutTimeMap.get(student);
                    deque.add(reportModel.getDateTime());
                } else {
                    studentOutTimeMap.put(student,new ArrayDeque<String>(Collections.singleton(reportModel.getDateTime())));
                }
            }
        });

        studentInTimeMap.forEach((student, inTimes) -> {
            if(studentOutTimeMap.get(student).size() < inTimes.size()){
                Deque<String> deque = studentOutTimeMap.get(student);
                deque.add(new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime()));
            }
            if (studentOutTimeMap.get(student).size() == inTimes.size()) {
                Deque<String> outTime = studentOutTimeMap.get(student);
                final String[] totalTime = {"00:00:00"};
                inTimes.forEach(t -> {
                    totalTime[0] = totalTimeCalculate(timeDifference(t, outTime.removeFirst()), totalTime[0]);
                });
                if (totalTime[0].startsWith("12")) {
                    String[] s = totalTime[0].split(":");
                    s[0] = "00";
                    totalTime[0] = s[0] + ":" + s[1] + ":" + s[2];
                }
                stayTimePerStudents.add(new StayTimePerStudent(student.getName(), student.getRoll(), student.getBatch(), student.getDepart(), totalTime[0]));
            }
        });

    }

    public static String timeDifference(String startTime, String endTime) {
        LocalTime initialTime = LocalTime.parse(startTime);
        LocalTime finalTime =LocalTime.parse(endTime);
        StringJoiner joiner = new StringJoiner(":");
        long hours = initialTime.until( finalTime, ChronoUnit.HOURS);
        initialTime = initialTime.plusHours( hours );
        long minutes = initialTime.until(finalTime, ChronoUnit.MINUTES);
        initialTime = initialTime.plusMinutes( minutes );
        long seconds = initialTime.until( finalTime, ChronoUnit.SECONDS);
        joiner.add(String.valueOf(hours));
        joiner.add(String.valueOf(minutes));
        joiner.add(String.valueOf(seconds));
        return joiner.toString();
    }

    public static String totalTimeCalculate(String time, String time1) {
        String[] HMS = time1.split(":");
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.SECOND, Integer.parseInt(HMS[2]));
        cal.add(Calendar.MINUTE, Integer.parseInt(HMS[1]));
        cal.add(Calendar.HOUR, Integer.parseInt(HMS[0]));
        return format.format(cal.getTime());
    }
}
