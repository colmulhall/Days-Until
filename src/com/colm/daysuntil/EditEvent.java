package com.colm.daysuntil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.daysuntil.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditEvent extends Activity
{
	private DBManager db;
	private TextView eventTitle, eventDate;
	private EditText editTitle;
	private DatePicker editDate;
	private Button updateEvent;
	private String passedValue;
	public int id;
	public final static String ID_EXTRA = "com.colm.daysuntil._ID";
	
	// data entered
	private String title, date, readable_date, color;
	
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editevent_layout);
        
        // intent passed data
     	passedValue = getIntent().getStringExtra(MainScreen.ID_EXTRA);
     	id = Integer.parseInt(passedValue);
     	
        getActionBar().setDisplayHomeAsUpEnabled(true); // for up navigation
        
        eventTitle = (TextView)findViewById(R.id.eventtitle);
        eventDate = (TextView)findViewById(R.id.eventdate);
        editTitle = (EditText)findViewById(R.id.edittitle);
        editDate = (DatePicker)findViewById(R.id.editdate);
        updateEvent = (Button)findViewById(R.id.updateevent);
        updateEvent.setOnClickListener(buttonAddOnClickListener);
        
        // Open database to write
        db = new DBManager(this);
        db.openToWrite();
        
        // set the data from the db
        setupData();
    }
    
    // edit the event
    Button.OnClickListener buttonAddOnClickListener = new Button.OnClickListener()
    {  
    	@Override
	    public void onClick(View arg0) 
    	{
		    // get the data from the fields
		    getData();
		    	
		    // perform validation and the update
		    if(dateIsInThePast())
		    {
		    	Toast.makeText(getApplicationContext(), "Please enter a future date", Toast.LENGTH_LONG).show();
		    }
		    else if(title == null || title.isEmpty())
		    {
		    	Toast.makeText(getApplicationContext(), "Please enter an event title", Toast.LENGTH_LONG).show();
		    }
		    else
		    {
		    	readable_date = dateToNiceString();  // set the readable date for the update
		    	
		    	db.update(id, title, date, readable_date, color);
		    	Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
		    }
    	}
    };
    
    // set the data that is to be edited
    public void setupData()
    {
    	// set the event title
    	editTitle.setText(db.getEventTitle(id));
    	
    	// set the event date
    	String theDateFromTheDB = db.getDate(id);
    	String year = theDateFromTheDB.substring(0, 4);
    	String month = theDateFromTheDB.substring(4, 6);
    	String day = theDateFromTheDB.substring(6, 8);
    	
    	// set the month to the previous month. (weird glitch)
    	switch(month)
    	{
    		case "01":
    			month = "12";
    			break;
    		case "02":
    			month = "01";
    			break;
    		case "03":
    			month = "02";
    			break;
    		case "04":
    			month = "03";
    			break;
    		case "05":
    			month = "04";
    			break;
    		case "06":
    			month = "05";
    			break;
    		case "07":
    			month = "06";
    			break;
    		case "08":
    			month = "07";
    			break;
    		case "09":
    			month = "08";
    			break;
    		case "10":
    			month = "09";
    			break;
    		case "11":
    			month = "10";
    			break;
    		case "12":
    			month = "11";
    			break;
    		default:
    			break;
    	}
    	
    	// another weird glitch where the year get automatically incremented when the month is January
    	if(month == "12")
    		editDate.updateDate(Integer.parseInt(year)-1, Integer.parseInt(month), Integer.parseInt(day));
    	else
    		editDate.updateDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }
    
    // get the entered data
    public void getData()
    {
    	String getTitle = editTitle.getText().toString();
    	
	    int getYear = editDate.getYear();
	    int getMonth = editDate.getMonth()+1;
	    int getDay = editDate.getDayOfMonth();
	    
	    // set the data to the variables declared above
	    title = getTitle;

	    // ugly date formatting stuff
	    if(getDay < 10 && getMonth < 10)
	    {
		    DecimalFormat df = new DecimalFormat("00");
		    String m = df.format(getMonth);
		    String d = df.format(getDay);
		    date = "" + getYear + "" + m + "" + d;
	    }
	    else if(getDay < 10 && getMonth >= 10)
	    {
	    	DecimalFormat df = new DecimalFormat("00");
	    	String d = df.format(getDay);
	    	date = "" + getYear + "" + getMonth + "" + d;
	    }
	    else if(getMonth < 10 && getDay >= 10)
	    {
	    	DecimalFormat df = new DecimalFormat("00");
	    	String m = df.format(getMonth);
	    	date = "" + getYear + "" + m + "" + getDay;
	    }
	    else
	    	date = "" + getYear + "" + getMonth + "" + getDay;
    }
    
    // check if the entered date is in the past
    @SuppressLint("SimpleDateFormat")
	public boolean dateIsInThePast()
    {
    	SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
    	
    	// get todays date
    	Date now = new Date();
    	
    	String year = date.substring(0, 4);
    	String month = date.substring(4, 6);
    	String day = date.substring(6, 8);
    	String testDate = day + " " + month + " " + year;
    	
    	try 
    	{
			if (myFormat.parse(testDate).before(now)) 
			{
				// date is in the past
				return true;
			}
		} 
    	catch (ParseException e1) 
    	{
			e1.printStackTrace();
		}
    	return false;
    }
    
    // get a string from a date. (e.g. 01-01-2014 will return as 1 Jan 2014)
    public String dateToNiceString()
    {
    	String year = date.substring(0, 4);
    	String month = date.substring(4, 6);
    	String day = date.substring(6, 8);
    	
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
    
    // for up navigation
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
    	{
	    	case android.R.id.home:
	    		NavUtils.navigateUpFromSameTask(this);
	    		overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);  //animation
	    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() 
    {
    	moveTaskToBack(true); 
        EditEvent.this.finish();
    }
    
    // life cycles
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