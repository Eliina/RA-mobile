package com.example.vardadienuwidgets;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

public class NameWidgetAlarmManager extends BroadcastReceiver{
public static final String ALARM_ACTION = "com.example.vardadienuwidgets.ALARM_ACTION";

private static final long UPDATE_FREQUENCY = (1000*10);
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals(NameWidgetAlarmManager.ALARM_ACTION))
		{
			
		    Intent updateIntent = new Intent(WidgetProvider.UPDATE_WIDGETS);   
		    context.sendBroadcast(updateIntent);
		}
	}
	public static void setAlarm(Context context){
		AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(NameWidgetAlarmManager.ALARM_ACTION);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime()+ NameWidgetAlarmManager.UPDATE_FREQUENCY,
				NameWidgetAlarmManager.UPDATE_FREQUENCY, pIntent);
	
	}
	public static void clearAlarm(Context context){
		AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(NameWidgetAlarmManager.ALARM_ACTION);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pIntent);
		
	}

	
	
	
	
	
}