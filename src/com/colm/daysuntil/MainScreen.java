package com.colm.daysuntil;

import org.joda.time.Days;
import org.joda.time.LocalDate;
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
    public String daysUntil(LocalDate eventDate)
    {
    	LocalDate todaysDate = new LocalDate();
        LocalDate endDate = new LocalDate(2014, 12, 25);
        
        String daysBetween = Days.daysBetween(todaysDate, endDate).toString();
        daysBetween = daysBetween.substring(1, daysBetween.length()-1);
    	 
    	return daysBetween;
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