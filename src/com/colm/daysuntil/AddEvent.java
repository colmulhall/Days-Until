package com.colm.daysuntil;

import com.example.daysuntil.R;

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

public class AddEvent extends Activity
{
	private DBManager db;
	private TextView eventTitle, eventDate;
	private EditText enterTitle;
	private DatePicker enterDate;
	private Button addEvent;
	
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
        addEvent = (Button)findViewById(R.id.addevent);
        addEvent.setOnClickListener(buttonAddOnClickListener);
        
        //Open database to write
        db = new DBManager(this);
        db.openToWrite();
    }
    
    // add the new event
    Button.OnClickListener buttonAddOnClickListener = new Button.OnClickListener()
    {  
    	@Override
	    public void onClick(View arg0) 
    	{
		    // get the data from the fields
		    getData();
		    	
		    // insert the data into the database
		    db.insert(title, date);
			    
			Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
			    
			// reset event field after insert
			enterTitle.setText(null);
    	}
    };
    
    // get the entered data
    public void getData()
    {
    	String getTitle = enterTitle.getText().toString();
	    int getYear = enterDate.getYear();
	    int getMonth = enterDate.getMonth();
	    int getDay = enterDate.getDayOfMonth();
	    
	    // set the data to the variables declared above
	    title = getTitle;
	    date = "" + getYear + "" + getMonth + "" + getDay;
    }
    
    // validate the event title
    public boolean isEmpty(String title)
    {
    	if(title.equals(""))
    		return true;
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
	    		overridePendingTransition(R.anim.slide_out_left_to_right, R.anim.slide_in_left_to_right);  //animations
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