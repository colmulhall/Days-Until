package com.colm.daysuntil;

import com.example.daysuntil.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ColorPickerActivity extends Activity 
{
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() 
		{

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				Dialog dialog;
				switch (position) 
				{
					case 0:
						dialog = new ColorPickerDialog(ColorPickerActivity.this);
						dialog.show();
						break;
				}
			}
		});
	}
}