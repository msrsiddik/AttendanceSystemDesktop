package msr.attend.desktop.model;

public class AttendModel {
    private String UID;
    private String name;
    private String batch;
    private long dateTime;

    public AttendModel() {
    }

    public AttendModel(String UID, String name, String batch, long dateTime) {
        this.UID = UID;
        this.name = name;
        this.batch = batch;
        this.dateTime = dateTime;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
