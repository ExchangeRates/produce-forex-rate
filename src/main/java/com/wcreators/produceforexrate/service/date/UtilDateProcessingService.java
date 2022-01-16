package com.wcreators.produceforexrate.service.date;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class UtilDateProcessingService implements DateProcessingService<Date> {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @PostConstruct
    public void init() {
        System.out.println();
    }

    @Override
    public Date now() {
        return new Date(System.currentTimeMillis());
    }

    @Override
    public String dateToString(Date date) {
        return formatter.format(date);
    }

    @Override
    public int getMinutes(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

}
