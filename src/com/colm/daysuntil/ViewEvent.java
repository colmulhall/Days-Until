package com.colm.daysuntil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.example.daysuntil.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	public final static String ID_EXTRA = "com.mypackage.msdassignment._ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewevent);
		
		// intent passed data
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
    
    public String daysUntil()
    {
    	SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
    	
    	String year = db.getDate(id).substring(0, 4);
    	String month = db.getDate(id).substring(4, 6);
    	String day = db.getDate(id).substring(6, 8);
    	
    	Date now = new Date();
    	String todaysDate = myFormat.format(now);
    	String eventDate = day + " " + month + " " + year;
    	String daysUntil = "";
    	
    	try 
    	{
    	    Date date1 = myFormat.parse(todaysDate);
    	    Date date2 = myFormat.parse(eventDate);
    	    long diff = date2.getTime() - date1.getTime();
    	    
    	    // calculate the number of days between the two dates
    	    daysUntil = "Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    	} 
    	catch (ParseException e) 
    	{
    	    e.printStackTrace();
    	}
    	return daysUntil;
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
	    		overridePendingTransition(R.anim.slide_out_left_to_right, R.anim.slide_in_left_to_right);  //animations
	    		return true;
	    	
	    	// deleting the event
	    	case R.id.deleteevent:
	    		displayDialog();  // display "are you sure" dialog
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