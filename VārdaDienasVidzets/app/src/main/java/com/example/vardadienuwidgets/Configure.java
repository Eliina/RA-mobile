package com.example.vardadienuwidgets;


import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Spinner;

public class Configure extends Activity {
 
	
	static Configure context;
	private int widgetID;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure);
		setResult(RESULT_CANCELED);
		context=this;
		Bundle extras = getIntent().getExtras();
		if(extras!= null){
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

			
		final Spinner spinner = (Spinner)findViewById(R.id.Spinner2);
		final EditText Fontsize = (EditText)findViewById(R.id.sizeInput);	
			Button b= (Button) findViewById(R.id.button1);
			
			
			
			b.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {	
					int FontSize= Integer.parseInt(Fontsize.getText().toString());
					String FontColor = spinner.getSelectedItem().toString();
					NameWidgetSharedPreferences.savePreferences(FontSize,FontColor, widgetID, context);			
					WidgetProvider.widgetUpdate(context,widgetID);	
					
				      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
				       Intent intent = new Intent(context, Configure.class);
				       intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
				       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				       intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
				       PendingIntent pendIntent = PendingIntent.getActivity(context, widgetID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				       views.setOnClickPendingIntent(R.id.text, pendIntent);
					
					Intent resultValue = new Intent();
					resultValue.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
					resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
					setResult(RESULT_OK, resultValue);
					finish();				
				}
			});
	}


}

