package msr.attend.desktop.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println(calendar.getTime());
        long l = calendar.getTime().getTime();
        long n = l + TimeUnit.MINUTES.toMillis(5);
        System.out.println(new Date(n));
        System.out.println(df.format(new Date(n)));
    }
}
