package com.colm.daysuntil;

import java.util.ArrayList;
import java.util.List;

import com.example.daysuntil.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ColorPickerDialog extends Dialog 
{
	public List<Integer> colorList = new ArrayList<Integer>();
	AddEvent addevent;
	EditEvent editEvent;
	
	public ColorPickerDialog(Context context) 
	{
		super(context);
		this.setTitle("Pick Background");
		
		// create an instance of the add/edit event classes
		addevent = new AddEvent();
		editEvent = new EditEvent();
		
		// for convenience and better reading, we place the colors in a two dimension array
		String colors[][] = { { "822111", "AC2B16", "CC3A21", "E66550", "EFA093", "F6C5BE" }, 
							  { "A46A21", "CF8933", "EAA041", "FFBC6B", "FFD6A2", "FFE6C7" },
							  { "AA8831", "D5AE49", "F2C960", "FCDA83", "FCE8B3", "FEF1D1" }, 
							  { "076239", "0B804B", "149E60", "44B984", "89D3B2", "B9E4D0" },
							  { "1A764D", "2A9C68", "3DC789", "68DFA9", "A0EAC9", "C6F3DE" }, 
							  { "1C4587", "285BAC", "3C78D8", "6D9EEB", "A4C2F4", "C9DAF8" },
							  { "41236D", "653E9B", "8E63CE", "B694E8", "D0BCF1", "E4D7F5" }, 
							  { "83334C", "B65775", "E07798", "F7A7C0", "FBC8D9", "FCDEE8" },
							  { "000000", "434343", "666666", "999999", "CCCCCC", "EFEFEF" } };

		colorList = new ArrayList<Integer>();

		// add the color array to the list
		for (int i = 0; i < colors.length; i++) 
		{
			for (int j = 0; j < colors[i].length; j++) 
			{
				colorList.add(Color.parseColor("#" + colors[i][j]));
			}
		}
		
		for(int i=0; i<colorList.size(); i++)
			Log.i("HERE", ""+colorList.get(i));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_picker);
		
		GridView gridViewColors = (GridView) findViewById(R.id.gridViewColors);
		gridViewColors.setAdapter(new ColorPickerAdapter(getContext()));
		
		// close the dialog on item click
		gridViewColors.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				// set the selected color for the AddEvent/EditEvent classes to use
				AddEvent.selectedColor = colorList.get(position);  
				EditEvent.selectedColor = colorList.get(position);
				ColorPickerDialog.this.dismiss();
			}
		});
	}
}