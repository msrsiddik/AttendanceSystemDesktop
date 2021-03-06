package msr.attend.desktop.model;

public class AttendModel {
    private String UID;
    private String roll;
    private String name;
    private String batch;
    private String depart;
    private long dateTime;

    public AttendModel() {
    }

    public AttendModel(String UID, String roll, String name, String batch, String depart, long dateTime) {
        this.UID = UID;
        this.roll = roll;
        this.name = name;
        this.batch = batch;
        this.depart = depart;
        this.dateTime = dateTime;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
