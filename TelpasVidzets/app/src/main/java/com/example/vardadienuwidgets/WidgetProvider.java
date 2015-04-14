package com.example.vardadienuwidgets;


import  com.example.vardadienuwidgets.NameWidgetAlarmManager;
import com.example.vardadienuwidgets.WidgetProvider;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class WidgetProvider extends AppWidgetProvider
{

	public static final String UPDATE_WIDGETS ="com.example.vardadienuwidgets.UPDATE_WIDGETS";

    TextView mInfo;
    TextView mEvents;
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	    if (intent.getAction().equals(WidgetProvider.UPDATE_WIDGETS)) {
	    	AppWidgetManager appWidgetManager =AppWidgetManager.getInstance(context);
	    	int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
	       
	           onUpdate(context, appWidgetManager, ids);
	         
	        }
	    
	   
	}
	
	
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onDisabled(Context context)
	{
		super.onDisabled(context);
		Toast.makeText(context, "Disable", Toast.LENGTH_LONG).show();
		NameWidgetAlarmManager.clearAlarm(context);
	}

	@Override
	public void onEnabled(Context context)
	{
		super.onEnabled(context);
		Toast.makeText(context, "Enable", Toast.LENGTH_LONG).show();
		NameWidgetAlarmManager.setAlarm(context);
	}

	@Override
	public void onUpdate(
		Context context,
		AppWidgetManager appWidgetManager,
		int[] appWidgetIds
	)
	{
	
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		int count = appWidgetIds.length;
				for(int i=0; i<count; i++)
				{
                   WidgetProvider.widgetUpdate(context, appWidgetIds[i]);
				}
				
		
				
	}

public static void widgetUpdate(Context context,int widgetID) {

    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
    AppWidgetManager manager = AppWidgetManager.getInstance(context);
    int FontSize = NameWidgetSharedPreferences.getFontSize(widgetID, context);
    String FontColor = NameWidgetSharedPreferences.getFontColor(widgetID, context);
    if (netConnect(context)) {
        //String url = "http://apis.lv/namedays.json//?key=df09cce396b12009542ace5f9ffa362f80524364&date=";
        //HandleJSON obj;
        //obj = new HandleJSON(url);
        //obj.fetchJSON();
        //while(obj.parsingComplete);
        //StringBuilder result = obj.getNames();
        //views.setTextViewText(R.id.text, result);
        //views.setFloat(R.id.text,"setTextSize" ,FontSize);
        //views.setTextColor(R.id.text, Color.parseColor(FontColor));


        // public void onCreate(Bundle savedInstanceState) {
        //     super.onCreate(savedInstanceState);
        //    setContentView(R.layout.main);

        mInfo = (TextView) findViewById(R.id.info);

//events = layoutā ir izveidots kā trīs kolonnas: laiks, lekcija, auditorija
        mEvents = (TextView) findViewById(R.id.events);
        Button btnShow = (Button) findViewById(R.id.btn_show);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readCalendars("ITC 203");
            }
        });


    private void addEvents(ArrayList<CalendarEvent> calendarEvents) {

        TableLayout tl = (TableLayout) findViewById(R.id.TableLayout01);
        //izdzēst visas rindas
        tl.removeAllViews();

        //liek notikumus klat
        for (CalendarEvent currentEvent : calendarEvents) {
            TableRow tr = new TableRow(this);

            //laiks
            TextView laiks = new TextView(this);
            laiks.setText(currentEvent.getFormatedTime(true) + " - " + currentEvent.getFormatedTime(false));

            //lekcija, studentu grupa
            TextView lekcija = new TextView(this);
            lekcija.setText(currentEvent.lekcija + ", " + currentEvent.stud_grupa);

            //pasniedzejs
            TextView pasniedzejs = new TextView(this);
            pasniedzejs.setText(currentEvent.pasniedzejs);

            //izveido rindas, kur ievieto informaciju
            tr.addView(laiks);
            tr.addView(lekcija);
            tr.addView(pasniedzejs);
            tl.addView(tr, new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }


    private void readCalendars(String calendarName) {
        ArrayList<CalendarEvent> newEvents = null;
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        Cursor calCursor =
                getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");
        long calId = -1;
        String calendarAccount = "";
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String calName = calCursor.getString(1);
                if (calName != null && calName.equals(calendarName)) {
                    calendarAccount = calName + " " + calCursor.getString(2);
                    calId = id;
                    break;
                }
            } while (calCursor.moveToNext());
        }
        mInfo.setText(calendarAccount);
        if (calId != -1) {
            Calendar currTime = Calendar.getInstance();
            currTime.add(Calendar.HOUR, -4);
            long begin = currTime.getTimeInMillis(); // starting time in milliseconds
            currTime.add(Calendar.DATE, 30);
            long end = currTime.getTimeInMillis();// ending time in milliseconds
            String[] proj =
                    new String[]{
                            CalendarContract.Instances._ID,
                            CalendarContract.Instances.BEGIN,
                            CalendarContract.Instances.END,
                            CalendarContract.Instances.EVENT_ID,
                            CalendarContract.Instances.DESCRIPTION,
                            CalendarContract.Instances.EVENT_LOCATION,
                            CalendarContract.Instances.TITLE};


            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, begin);
            ContentUris.appendId(builder, end);

            Cursor cursor = getContentResolver().query(builder.build(),
                    proj, CalendarContract.Instances.CALENDAR_ID + " =" + calId,
                    null, "begin ASC");

            String events = "";
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    int i = 0;

                    // Date date1 = null;
                    // try{
                    //     String string = "24.03.2015. 10:30";
                    //      DateFormat format = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
                    //     date1 = format.parse(string);
                    //  }catch(ParseException e){
                    // TODO Auto-generated catch block
                    //     e.printStackTrace();
                    //   }
                    do {
                        long eventId = cursor.getLong(3);
                        events += getEventInfo(eventId, cursor.getLong(1), cursor.getLong(2), cursor.getString(4), cursor.getString(5), cursor.getString(6))
                                + "\n";
                        CalendarEvent currentEvent = new CalendarEvent();
                        currentEvent.sLaiks = new Date(cursor.getLong(1));
                        currentEvent.bLaiks = new Date(cursor.getLong(2));

                        //iegūst pasreizējo laiku
                        java.util.Date date = new java.util.Date();
                        long time = date.getTime();
                        long now = System.currentTimeMillis();
                        java.util.Date Now = new java.util.Date(now);


                        //pārbauda vai lekcija notiek pašreizējā laikā
                        if (currentEvent.bLaiks.after(Now)) {

                            // if (currentEvent.sLaiks.before(Now)){
                            //     i++;
                            // }else{

                            // }
                            i++;
                            if (currentEvent.bLaiks.after(Now) && currentEvent.sLaiks.after(Now)) {
                                //pārbauda vai ir kāds notikums. Ja nav tad veido jaunu sarakstu
                                if (newEvents == null) {
                                    newEvents = new ArrayList<CalendarEvent>();
                                }
                                currentEvent.pasniedzejs = cursor.getString(4);
                                currentEvent.auditorija = cursor.getString(5);
                                currentEvent.lekcija = cursor.getString(6);

                                newEvents.add(currentEvent);
                            }
                        }
                    } while (cursor.moveToNext() && i < 4);
                }

            }
            mEvents.setText(events);

        }
        addEvents(newEvents);
    }


    private String getEventInfo(long eventId, long sLaiks, long bLaiks, String pasniedzejs, String auditorija, String lekcija) {
        String eventInfo = "";
//            long selectedEventId = eventId;
//                    String[] proj =
//                new String[]{
//                        CalendarContract.Events._ID,
//                        CalendarContract.Events.DTSTART,
//                        CalendarContract.Events.DTEND,
//                        CalendarContract.Events.EVENT_LOCATION,
//                        CalendarContract.Events.TITLE};
//            Cursor cursor =
//                    getContentResolver().
//                            query(
//                                    CalendarContract.Events.CONTENT_URI,
//                                    proj,
//                                    CalendarContract.Events._ID + " = ? ",
//                                    new String[]{Long.toString(selectedEventId)},
//                                    null);
//            if (cursor.moveToFirst())
        {
            DateFormat df = new SimpleDateFormat("HH:mm");
            String start = df.format(new Date(sLaiks));
            String end = df.format(new Date(bLaiks));
            eventInfo = start + "-" + end + " " + auditorija + " " + lekcija + " " + pasniedzejs;
        }
        return eventInfo;
    }
}


	else {
		
		views.setTextViewText(R.id.text, "Nav interneta savienojuma");	
		views.setFloat(R.id.text,"setTextSize" ,FontSize);
		views.setTextColor(R.id.text, Color.parseColor(FontColor));
		
	}
	  Intent intent = new Intent(context, Configure.class);
	  intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
      PendingIntent pendIntent = PendingIntent.getActivity(context, widgetID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      views.setOnClickPendingIntent(R.id.text, pendIntent);
      manager.updateAppWidget(widgetID, views);
	
	
	
	
}
			
			
			
			
	

	public static boolean netConnect(Context ctx) //Interneta savienojuma parbaudei
	{
		ConnectivityManager cm;
		NetworkInfo info = null;
		
		try {
			cm = (ConnectivityManager) 
			ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = cm.getActiveNetworkInfo();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		if (info != null){
			return true;
		}
		else {
			return false;
		}
	}


}