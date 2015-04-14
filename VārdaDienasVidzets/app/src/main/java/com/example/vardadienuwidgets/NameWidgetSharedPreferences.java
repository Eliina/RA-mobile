package com.example.vardadienuwidgets;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class NameWidgetSharedPreferences {

	
public static void savePreferences(int FontSize, String FontColor,int widgetID, Context context) {
Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
editor.putInt("FontSize" + widgetID, FontSize);
editor.putString("FontColor" + widgetID, FontColor);
editor.commit();
	 }
public static int getFontSize(int widgetID, Context context)
{	
SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
int FontSize = prefs.getInt("FontSize" + widgetID, 25);
return FontSize;
}
public static String getFontColor(int widgetID, Context context)
{	
SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
String FontColor = prefs.getString("FontColor" + widgetID, "#FFFFFF");
return FontColor;
}	
public static void deletePreferences(int widgetID, Context context) {
	Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();	
	editor.remove("FontSize" + widgetID);
	editor.remove("FontColor" + widgetID);
	editor.commit();	
}	

}

