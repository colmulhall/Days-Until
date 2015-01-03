package com.colm.daysuntil;

import com.example.daysuntil.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class ColorPickerDialog extends Dialog 
{

	public ColorPickerDialog(Context context) 
	{
		super(context);
		this.setTitle("Pick Background");
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
				ColorPickerDialog.this.dismiss();
			}
		});
	}
	
	// ------------------
//	public interface OnColorPickedListener 
//	{
//		public abstract void colorPicked(int color);
//	}
//	
//	OnColorPickedListener colorPickedListener;
//
//	public void setOnColorPickedListener(OnColorPickedListener colorPickedListener) 
//	{
//		this.colorPickedListener = colorPickedListener;
//	}
//	
//	// this is the click on the grid view
//	public void onItemClick(AdapterView< ?> parent, View view, int position, long id) 
//	{
//		// get myColor from adapter and notify the listener
//		if(colorPickedListener != null)
//			colorPickedListener.colorPicked(position);
//	}
}