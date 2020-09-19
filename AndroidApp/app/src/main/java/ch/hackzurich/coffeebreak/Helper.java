package ch.hackzurich.coffeebreak;

import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public final class Helper {

    public static Date getDateFromTimePicker(TimePicker datePicker){
        int year, month, date, hour, minute;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);

        hour = datePicker.getHour();
        minute =  datePicker.getMinute();

        calendar.set(year, month, date, hour, minute);

        return calendar.getTime();
    }

}
