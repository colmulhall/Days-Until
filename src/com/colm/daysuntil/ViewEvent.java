package com.colm.daysuntil;

import java.util.Date;

import org.joda.time.DateMidnight;
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
    	
        //int days = daysUntil();
        
        //days_until_event.setText(daysUntil());
        
        event_date.setText(db.getDate(id));
    }
	
	// calculate the days between the two dates. This uses the Jodatime library
    public int daysUntil()
    {
    	LocalDate start = new LocalDate(2014, 01, 01);
    	LocalDate end = new LocalDate(2015, 03, 04);
    	
    	int daysBetween = Days.daysBetween(start, end).getDays();
    	 
    	return daysBetween;
    }
        
//	//action bar
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