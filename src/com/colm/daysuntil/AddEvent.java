package com.colm.daysuntil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.daysuntil.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEvent extends Activity
{
	private DBManager db;
	private TextView eventTitle, eventDate;
	private static TextView colorSample;
	private EditText enterTitle;
	private DatePicker enterDate;
	private Button addEvent, pickColor;
	private ColorPickerDialog colorPickerDialog;
	
	// selected color from the dialog
	static int selectedColor = 0;
	
	// data entered
	private String title, date, readable_date, color;
	
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent_layout);
        
        getActionBar().setDisplayHomeAsUpEnabled(true); // for up navigation
        
        // stop keyboard automatically opening
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        
        eventTitle = (TextView)findViewById(R.id.eventtitle);
        eventDate = (TextView)findViewById(R.id.eventdate);
        enterTitle = (EditText)findViewById(R.id.entertitle);
        enterDate = (DatePicker)findViewById(R.id.enterdate);
        pickColor = (Button)findViewById(R.id.pickcolor);
        pickColor.setOnClickListener(buttonPickColorOnClickListener);
        colorSample = (TextView)findViewById(R.id.colorsample);
        addEvent = (Button)findViewById(R.id.addevent);
        addEvent.setOnClickListener(buttonAddOnClickListener);
        
        // Open database to write
        db = new DBManager(this);
        db.openToWrite();
    }
    
    // pick a background color
    Button.OnClickListener buttonPickColorOnClickListener = new Button.OnClickListener()
    {  
    	@Override
	    public void onClick(View arg0) 
    	{
    		// display the color picker dialog
    		Dialog dialog;
    		dialog = new ColorPickerDialog(AddEvent.this, "AddEvent");
			dialog.show();
			changeSampleColor();
    	}
    };
    
    // add the new event
    Button.OnClickListener buttonAddOnClickListener = new Button.OnClickListener()
    {  
    	@Override
	    public void onClick(View arg0) 
    	{
		    // get the data from the fields
		    getData();
		    	
		    // perform validation and the insert
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
		    	readable_date = dateToNiceString();  // set the readable date for the insertion
		    	
		    	color = ""+selectedColor;   // get selected color from the dialog
		    	
		    	db.insert(title, date, readable_date, color);
		    	Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
		    	
		    	// reset event field after insert
				enterTitle.setText(null);
				
				// reset color
				selectedColor = -1052689;
		    }
    	}
    };
    
    // get the entered data
    public void getData()
    {
    	String getTitle = enterTitle.getText().toString();
    	
	    int getYear = enterDate.getYear();
	    int getMonth = enterDate.getMonth()+1;
	    int getDay = enterDate.getDayOfMonth();
	    
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
    	
    	// get the selected date
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
    	
    	// remove the leading zero
    	if(Integer.parseInt(day) < 10)
    		day = day.replace("0", "");
    	
    	return day + " " + month + " " + year;
    }
    
    // change the color of the sample selected color textview
    public static void changeSampleColor()
    {
    	String hexColor = String.format("#%06X", (0xFFFFFF & selectedColor));
	    GradientDrawable bgShape = (GradientDrawable)colorSample.getBackground();
	    bgShape.setColor(Color.parseColor(hexColor));
    }
    
    // for up navigation
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
    	{
	    	case android.R.id.home:
	    		NavUtils.navigateUpFromSameTask(this);
	    		overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);  // animations
	    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() 
    {
    	moveTaskToBack(true); 
        AddEvent.this.finish();
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