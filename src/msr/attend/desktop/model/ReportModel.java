package msr.attend.desktop.model;

public class ReportModel {
    private String roll;
    private String name;
    private String batch;
    private String depart;
    private String dateTime;
    private String status;

    public ReportModel(String roll, String name, String batch, String depart, String dateTime, String status) {
        this.roll = roll;
        this.name = name;
        this.batch = batch;
        this.depart = depart;
        this.dateTime = dateTime;
        this.status = status;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
