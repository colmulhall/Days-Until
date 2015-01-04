package com.colm.daysuntil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.example.daysuntil.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ViewEvent extends Activity
{
	private DBManager db;
	private String passedValue;
	private TextView event_title, days_until_event, event_date;
	public int id;
	public final static String ID_EXTRA = "com.colm.daysuntil._ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewevent);
		getWindow().getDecorView().setBackgroundColor(Color.MAGENTA);
		
		// intent passed data
		passedValue = getIntent().getStringExtra(MainScreen.ID_EXTRA);
		id = Integer.parseInt(passedValue);
		
		event_title = (TextView)findViewById(R.id.eventtitle);
		days_until_event = (TextView)findViewById(R.id.daysuntilevent);
		event_date = (TextView)findViewById(R.id.eventdate);
		
		// get the data from the DB
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
        event_date.setText(dateToNiceString());
    }
    
	// calculate the days between the two dates
    @SuppressLint("SimpleDateFormat")
	public String daysUntil()
    {
    	SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
    	
    	// format the date from the database
    	String theDateFromTheDB = db.getDate(id);
    	String year = theDateFromTheDB.substring(0, 4);
    	String month = theDateFromTheDB.substring(4, 6);
    	String day = theDateFromTheDB.substring(6, 8);
    	
    	// get todays date
    	Date now = new Date();
    	String todaysDate = myFormat.format(now);
    	
    	// ...and the date for this event from the database
    	String eventDate = day + " " + month + " " + year;
    	String daysUntil = "";
    	
    	
    	// calculate the number of days between the two dates
    	try 
    	{
    	    Date date1 = myFormat.parse(todaysDate);
    	    Date date2 = myFormat.parse(eventDate);
    	    long diff = date2.getTime() - date1.getTime();
    	    
    	    if(diff > 1)
    	    	daysUntil = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " days";
    	    else if(diff == 1)
    	    	daysUntil = "Tomorrow";
    	    else if(diff == 0)
    	    	daysUntil = "Today";
    	    else
    	    	daysUntil = "This event has passed";
    	}
    	catch (ParseException e) 
    	{
    	    e.printStackTrace();
    	}
    	return daysUntil;
    }
    
    // get a string from a date. (e.g. 01-01-2014 will return as 1 Jan 2014)
    public String dateToNiceString()
    {
    	String theDateFromTheDB = db.getDate(id);
    	String year = theDateFromTheDB.substring(0, 4);
    	String month = theDateFromTheDB.substring(4, 6);
    	String day = theDateFromTheDB.substring(6, 8);
    	
    	switch(month)
    	{
    		case "01":
    			month = "Jan";
    			break;
    		case "02":
    			month = "Feb";
    			break;
    		case "03":
    			month = "Mar";
    			break;
    		case "04":
    			month = "Apr";
    			break;
    		case "05":
    			month = "May";
    			break;
    		case "06":
    			month = "Jun";
    			break;
    		case "07":
    			month = "Jul";
    			break;
    		case "08":
    			month = "Aug";
    			break;
    		case "09":
    			month = "Sep";
    			break;
    		case "10":
    			month = "Oct";
    			break;
    		case "11":
    			month = "Nov";
    			break;
    		case "12":
    			month = "Dec";
    			break;
    		default:
    			break;
    	}
    	
    	return day + " " + month + " " + year;
    }
        
	// action bar
    @Override
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
    	//Inflate the menu. This adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewevent_menu, menu);
        return true;
	 }
    
    // for up navigation
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
    	{
    		// up navigation back pressed
	    	case android.R.id.home:
	    		NavUtils.navigateUpFromSameTask(this);
	    		overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);  //animation
	    		return true;
	    		
	    	// edit the event
	    	case R.id.editevent:
	    		Intent intent = new Intent(ViewEvent.this, EditEvent.class);
	    		intent.putExtra(ID_EXTRA, String.valueOf(id)); 
	    		startActivity(intent);
	    		overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);  //animation
				break;
	    	
	    	// deleting the event
	    	case R.id.deleteevent:
	    		displayDialog();  // display "are you sure" dialog
	    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    public void displayDialog()
    {
    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
        {
			//"Are you sure?" dialog options
            @Override
            public void onClick(DialogInterface dialog, int which) 
            {
                switch(which)
                {
	                case DialogInterface.BUTTON_POSITIVE:
	                    //Yes button clicked
	                	db.deleteEvent(id);
	    	    		Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
	    	    		overridePendingTransition(R.anim.slide_out_left_to_right, R.anim.slide_in_left_to_right);  //animations
	    	    		finish();
	                    break;
	
	                case DialogInterface.BUTTON_NEGATIVE:
	                    //No button clicked
	                    break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewEvent.this);
        builder.setTitle("Delete this event");
        builder.setMessage("Are you sure?")
        .setPositiveButton("Yes", dialogClickListener)
        .setNegativeButton("No", dialogClickListener)
        .show();
    }
    
    @Override
    public void onBackPressed() 
    {
    	moveTaskToBack(true); 
        ViewEvent.this.finish();
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