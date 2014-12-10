package com.colm.daysuntil;

import com.example.daysuntil.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class AddEvent extends Activity
{
	private DBManager db;
	private TextView eventTitle, eventDate;
	private EditText enterTitle;
	private DatePicker enterDate;
	
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent);
        
        getActionBar().setDisplayHomeAsUpEnabled(true); // for up navigation
        
        eventTitle = (TextView)findViewById(R.id.eventtitle);
        eventDate = (TextView)findViewById(R.id.eventdate);
        enterTitle = (EditText)findViewById(R.id.entertitle);
        enterDate = (DatePicker)findViewById(R.id.enterdate);
        
        //Open database to write
        db = new DBManager(this);
        db.openToWrite();
        
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
    
    // for up naviation
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
    	{
	    	case android.R.id.home:
	    		NavUtils.navigateUpFromSameTask(this);
	    		overridePendingTransition(R.anim.slide_out_left_to_right, R.anim.slide_in_left_to_right);  //animation
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