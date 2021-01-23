package msr.attend.desktop.model;

public class StayTimePerStudent extends Student{
    private String totalTime;

    public StayTimePerStudent(String name, String roll, String batch, String depart, String totalTime) {
        super(name, roll, batch, depart);
        this.totalTime = totalTime;
    }


    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}
