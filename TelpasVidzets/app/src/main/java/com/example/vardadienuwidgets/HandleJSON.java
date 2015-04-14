package com.example.vardadienuwidgets;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONObject;

public class HandleJSON //JSON datnes ieladei un nolasisanai. Darbojas tapat ka lietotnes versija
{
	StringBuilder messages = new StringBuilder();
	private String urlString;
	public volatile boolean parsingComplete = true;
	
	public HandleJSON(String url)
	{
		this.urlString = url;
	}

	public StringBuilder getNames()
	{
		return messages;
	}

	public void readAndParseJSON(String in)
	{
		Calendar c = Calendar.getInstance();
		int m = c.get(Calendar.MONTH);
		int d = c.get(Calendar.DAY_OF_MONTH);
		
		try {
			JSONArray jsonArray = new JSONArray(in);
			String sep = "";
			
			for (int index = 0; index < jsonArray.length(); index++) {
				JSONObject jsonObject = jsonArray.getJSONObject(index);
				
				if (jsonObject.getInt("month") == m+1 &&  jsonObject.getInt("day") == d) {	
					messages.append(sep);
					messages.append(jsonObject.getString("name")); 
					sep = ", ";
				}
			}
			
			parsingComplete = false;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fetchJSON()
	{
		Thread thread = new Thread(
			new Runnable()
			{
				@Override
				public void run()
				{
					try {
						URL url = new URL(urlString);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setReadTimeout(10000);
						conn.setConnectTimeout(15000);
						conn.setRequestMethod("GET");
						conn.setDoInput(true);
						
						conn.connect();
						InputStream stream = conn.getInputStream();
						String data = convertStreamToString(stream);
						readAndParseJSON(data);
						stream.close();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		);

		thread.start(); 		
	}
	
	static String convertStreamToString(java.io.InputStream is)
	{
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		
		return s.hasNext() ? s.next() : "";
	}
}
