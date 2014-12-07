package com.colm.daysuntil;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import com.example.daysuntil.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainScreen extends Activity implements AdapterView.OnItemClickListener
{
	private DBManager db;
	private Cursor cursor;
	private TextView header;
	private ListView listContent;
	public final static String ID_EXTRA = "com.mypackage.msdassignment._ID";

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        header = (TextView) findViewById(R.id.header_text); 
        
        listContent = (ListView)findViewById(R.id.list);
        header = (TextView) findViewById(R.id.header_text); 
        
        // setup the data from the database
        setupData();
    }
    
    public void setupData()
    {
    	//Open database to read
        db = new DBManager(this);
        db.openToRead();
        
        cursor = db.orderList();
        
        String[] from = new String[]{
        		DBManager.KEY_ID, 
        		DBManager.KEY_TITLE, 
        		DBManager.KEY_DATE,
        		DBManager.KEY_DAYS_UNTIL};
        int[] to = new int[]{R.id.eventtext};

        @SuppressWarnings("deprecation")
		SimpleCursorAdapter cursorAdapter =  new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
        
        listContent.setAdapter(cursorAdapter);
        listContent.setOnItemClickListener(this);
    }
    
    // calculate the days between the two dates. This uses the Jodatime library
    public Days daysUntil(DateTime eventDate)
    {
    	DateTime start = new DateTime(2004, 12, 25, 0, 0, 0, 0);
    	DateTime end = new DateTime(2006, 1, 1, 0, 0, 0, 0);

    	// period of 1 year and 7 days
    	Period period = new Period(start, end);

    	// calc will equal end
    	DateTime calc = start.plus(period);

    	// able to calculate whole days between two dates easily
    	Days days_between = Days.daysBetween(start, end);
    	
    	return days_between;
    }
    
    //action bar
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//Inflate the menu. This adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
        return true;
	}
    
    // handle click on a list item
    @Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
    {
    	Intent i = new Intent(MainScreen.this, DisplayEvent.class);
    	i.putExtra(ID_EXTRA, String.valueOf(id));  //pass the id of the selected item with the intent
    	startActivity(i);
//    	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);  //animation
	}
    
    // life cycles
    @Override
    protected void onPause()
    {
	    super.onPause();
	    db.close();
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	db.close();
    	finish();
    }
    
	@Override
	protected void onResume()
	{
		super.onResume();
		this.onCreate(null); //refresh the activity once it resumes 
	}
}