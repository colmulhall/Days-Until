/*package com.colm.daysuntil;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddEvent extends Activity
{
	private DBManager db;
	private TextView enterCountry, selectYear, selectMonth, selectTransport, enterDescription;
	private EditText editDesc;
	private Spinner spinYear, spinMonth, spinTransport;
	private Button add, back;
	private AutoCompleteTextView editCountry;
	
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcountry_layout);
        
        //Enter country
        enterCountry = (TextView)findViewById(R.id.enter_country);
        editCountry = (AutoCompleteTextView)findViewById(R.id.editcountry);  //autocomplete countries
        editCountry.setThreshold(1);
        
        //Enter year
        selectYear = (TextView)findViewById(R.id.enter_year);
        spinYear = (Spinner)findViewById(R.id.yearspin);
        
        //list of years for the spinner. gets current year and goes back to 1960
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1960; i--) 
        {
            years.add("" + i);
        }
        
        //Enter month
        selectMonth = (TextView)findViewById(R.id.enter_month);
        selectMonth.setTypeface(font);
        spinMonth = (Spinner)findViewById(R.id.monthspin);
        spinMonth.setBackgroundColor(inputBgColour);
        
        //Enter transport mode
        selectTransport = (TextView)findViewById(R.id.enter_transport);
        selectTransport.setTypeface(font);
        spinTransport = (Spinner)findViewById(R.id.transportspin);
        spinTransport.setBackgroundColor(inputBgColour);
        
        //Enter description
        enterDescription = (TextView)findViewById(R.id.enter_desc);
        editDesc = (EditText)findViewById(R.id.editdesc);
        editDesc.setBackgroundColor(inputBgColour);
        
        //Buttons
        add = (Button)findViewById(R.id.add);
        back = (Button)findViewById(R.id.backmain);
        
        //Open database to write
        db = new DBManager(this);
        db.openToWrite();
        
        add.setOnClickListener(buttonAddOnClickListener);  //listener for add country button
        
        //handle switching back to main screen
        back.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				db.close();
				
				finish();
			}
        });
    }
    
    //insert new country button
    Button.OnClickListener buttonAddOnClickListener = new Button.OnClickListener()
    {  
    	@Override
	    public void onClick(View arg0) 
    	{
		    String country = editCountry.getText().toString();
		    int year = Integer.parseInt(spinYear.getSelectedItem().toString());
		    String month = String.valueOf(spinMonth.getSelectedItem());
		    String transport = String.valueOf(spinTransport.getSelectedItem());
		    String desc = editDesc.getText().toString();
		
		    //check if the user has entered in a country name. if not do not allow them to continue
		    if(country.equals(""))
		    {
		    	Toast.makeText(getApplicationContext(), "You have not entered a country", Toast.LENGTH_LONG).show();
		    }
		    else
		    {
		    	//if everything is ok then enter the info into the database
		    	db.insert(country, year, month, transport, desc);
			    
			    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
			    
			    //reset edit fields and spinners after insert
			    editCountry.setText(null);
			    spinYear.setSelection(0, true);
			    spinMonth.setSelection(0, true);
			    spinTransport.setSelection(0, true);
			    editDesc.setText(null);
		    }
    	}
    };
    
    //action bar
    @Override
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
    	//Inflate the menu. This adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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
}*/