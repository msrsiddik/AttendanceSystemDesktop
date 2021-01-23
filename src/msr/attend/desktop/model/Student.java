package msr.attend.desktop.model;

import java.util.Objects;

public class Student {
    private String name;
    private String roll;
    private String batch;
    private String depart;

    public Student(String name, String roll, String batch, String depart) {
        this.name = name;
        this.roll = roll;
        this.batch = batch;
        this.depart = depart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) &&
                Objects.equals(roll, student.roll) &&
                Objects.equals(batch, student.batch) &&
                Objects.equals(depart, student.depart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, roll, batch, depart);
    }
}
