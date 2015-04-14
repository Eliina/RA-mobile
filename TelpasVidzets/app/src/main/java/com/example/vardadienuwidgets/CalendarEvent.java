package com.example.vardadienuwidgets;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ELINA on 2015.04.13..
 */
public class CalendarEvent {

        Date sLaiks;
        Date bLaiks;
        String lekcija;
        String auditorija;
        String stud_grupa;
        String kalendar_nos;
        String pasniedzejs;

        public String getFormatedTime (boolean startTime)
        {
            java.text.DateFormat df = new SimpleDateFormat("HH:mm");

            if(startTime){
                return df.format(sLaiks);
            }else {
                return df.format(bLaiks);
            }
        }


        public String getFormatedDate(boolean startDate){

            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd.MM.yy.");

            if(startDate){
                return simpleDateFormat.format(sLaiks);
            }else {
                return simpleDateFormat.format(bLaiks);
            }
        }

    }
