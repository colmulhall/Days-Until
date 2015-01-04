package com.colm.daysuntil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.daysuntil.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEvent extends Activity
{
	private DBManager db;
	private TextView eventTitle, eventDate;
	private EditText enterTitle;
	private DatePicker enterDate;
	private Button addEvent, pickColor;
	private ColorPickerDialog colorPickerDialog;
	
	// selected color from the dialog
	static int color = 0;
	
	// data entered
	private String title, date;
	
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent_layout);
        
        getActionBar().setDisplayHomeAsUpEnabled(true); // for up navigation
        
        eventTitle = (TextView)findViewById(R.id.eventtitle);
        eventDate = (TextView)findViewById(R.id.eventdate);
        enterTitle = (EditText)findViewById(R.id.entertitle);
        enterDate = (DatePicker)findViewById(R.id.enterdate);
        pickColor = (Button)findViewById(R.id.pickcolor);
        pickColor.setOnClickListener(buttonPickColorOnClickListener);
        addEvent = (Button)findViewById(R.id.addevent);
        addEvent.setOnClickListener(buttonAddOnClickListener);
        
        // Open database to write
        db = new DBManager(this);
        db.openToWrite();
        
        // get an instance of the dialog class
        colorPickerDialog = new ColorPickerDialog(AddEvent.this);
    }
    
    // pick a background color
    Button.OnClickListener buttonPickColorOnClickListener = new Button.OnClickListener()
    {  
    	@Override
	    public void onClick(View arg0) 
    	{
    		// display the color picker dialog
    		Dialog dialog;
    		dialog = new ColorPickerDialog(AddEvent.this);
			dialog.show();
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
		    	db.insert(title, date);
		    	Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
		    	
		    	// reset event field after insert
				enterTitle.setText(null);
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
    
//    // action bar
//    @Override
//	 public boolean onCreateOptionsMenu(Menu menu) 
//	 {
//    	//Inflate the menu. This adds items to the action bar if it is present.
//    	MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//	 }
    
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