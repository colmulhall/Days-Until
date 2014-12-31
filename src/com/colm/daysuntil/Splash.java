package com.colm.daysuntil;

import com.example.daysuntil.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity
{
    @Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.splash_layout);
	      
	     setProgressBarIndeterminateVisibility(true); 
	     
	     Thread timer = new Thread()
	     {
	    	 public void run()
	    	 {
	    		 try
	    		 {
	    			 sleep(1000);  // wait for 1 second
	    		 } 
	    		 catch(InterruptedException e)
	    		 {
	    			 e.printStackTrace();  // if error is found, send a message for debugging
	    		 } 
	    		 finally
	    		 {
	    			 Intent openMainScreen = new Intent(Splash.this, MainScreen.class);
	    			 startActivity(openMainScreen);
	    		 }
	    	 }
	     };
	     timer.start();
	 }

	 // kill the splash activity once the main activity starts
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		finish();
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		finish();
	}
}