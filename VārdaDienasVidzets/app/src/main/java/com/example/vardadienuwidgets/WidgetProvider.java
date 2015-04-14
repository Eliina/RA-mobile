package com.example.vardadienuwidgets;


import  com.example.vardadienuwidgets.NameWidgetAlarmManager;
import com.example.vardadienuwidgets.WidgetProvider;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;


public class WidgetProvider extends AppWidgetProvider
{

	public static final String UPDATE_WIDGETS ="com.example.vardadienuwidgets.UPDATE_WIDGETS";


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
			
public static void widgetUpdate(Context context,int widgetID)
{
	RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
	AppWidgetManager manager = AppWidgetManager.getInstance(context);
	int FontSize = NameWidgetSharedPreferences.getFontSize(widgetID, context);
	String FontColor = NameWidgetSharedPreferences.getFontColor(widgetID, context);
	if (netConnect(context)) {
	String url = "http://apis.lv/namedays.json//?key=df09cce396b12009542ace5f9ffa362f80524364&date=";
	HandleJSON obj;
	obj = new HandleJSON(url);
	obj.fetchJSON();
	while(obj.parsingComplete);
	StringBuilder result = obj.getNames();
	views.setTextViewText(R.id.text, result);
	views.setFloat(R.id.text,"setTextSize" ,FontSize);
	views.setTextColor(R.id.text, Color.parseColor(FontColor));

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
			
			
			
			
	

	public static boolean netConnect(Context ctx) //Interneta savienojuma pârbaudei
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