package com.colm.daysuntil;

import java.util.Date;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.example.daysuntil.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewEvent extends Activity
{
	private DBManager db;
	private String passedValue;
	private TextView event_title, days_until_event, event_date;
	public int id;
	public final static String ID_EXTRA = "com.mypackage.msdassignment._ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewevent);
		
		//intent passed data
		passedValue = getIntent().getStringExtra(MainScreen.ID_EXTRA);
		id = Integer.parseInt(passedValue);
		
		event_title = (TextView)findViewById(R.id.eventtitle);
		days_until_event = (TextView)findViewById(R.id.daysuntilevent);
		event_date = (TextView)findViewById(R.id.eventdate);
		
		setupData();
	}
	
	public void setupData()
    {
    	// Open database to read
        db = new DBManager(this);
        db.openToRead();
        
        // get the information from the database
        event_title.setText(db.getEventTitle(id));
        days_until_event.setText(daysUntil());
        event_date.setText(db.getDate(id));
    }
	
	// calculate the days between the two dates. This uses the Jodatime library
    public String daysUntil()
    {
    	// get the date from the db (very messy)
    	String year = db.getDate(id).substring(0, 3);
        String month = db.getDate(id).substring(4, 5);
        String day = db.getDate(id).substring(6, 7);
        
    	LocalDate todaysDate = new LocalDate();
        LocalDate endDate = new LocalDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        
        String daysBetween = Days.daysBetween(todaysDate, endDate).toString();
        daysBetween = daysBetween.substring(1, daysBetween.length()-1);
    	 
    	return daysBetween;
    }
        
//	//action bar
//    @Override
//	 public boolean onCreateOptionsMenu(Menu menu) 
//	 {
//    	//Inflate the menu. This adds items to the action bar if it is present.
//    	MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.country_menu, menu);
//        return true;
//	 }
//    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
        {
	      default:
	    	  	break;
        }
        return true;
    }
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		db.close();
		finish();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		db.close();
		finish();
	}
}