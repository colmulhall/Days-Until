package com.colm.daysuntil;

import com.example.daysuntil.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainScreen extends Activity implements AdapterView.OnItemClickListener
{
	private DBManager db;
	private Cursor cursor;
	private ListView listContent;
	public final static String ID_EXTRA = "com.mypackage.msdassignment._ID";

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        
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
        		DBManager.KEY_TITLE,
        		DBManager.KEY_DATE,
        		DBManager.KEY_ID};
        int[] to = new int[]{R.id.eventtext};

        @SuppressWarnings("deprecation")
		SimpleCursorAdapter cursorAdapter =  new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
        
        listContent.setAdapter(cursorAdapter);
        listContent.setOnItemClickListener(this);
    }
    
    // action bar
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//Inflate the menu. This adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.mainscreen_menu, menu);
        return true;
	}
    
    // handle click on a list item
    @Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
    {
    	Intent intent = new Intent(MainScreen.this, ViewEvent.class);
    	intent.putExtra(ID_EXTRA, String.valueOf(id));  //pass the id of the selected item with the intent
    	startActivity(intent);
    	overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);  //animation
	}
    
    // action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
        {
	      case R.id.add_action:
				Intent intent = new Intent(MainScreen.this, AddEvent.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);  //animation
				break;
				
	      case R.id.about_action:
	
	      default:
	    	  	break;
        }
        return true;
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