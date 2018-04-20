package de.ipcas.colorPicker;

import com.snagreporter.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class RainbowPickerDialog extends Dialog {

	public RainbowPickerDialog(Context context) {
		super(context);
		this.setTitle("RainbowPickerDialog");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_picker);

		GridView gridViewColors = (GridView) findViewById(R.id.gridViewColors);
		gridViewColors.setAdapter(new RainbowPickerAdapter(getContext()));

		// close the dialog on item click
		gridViewColors.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RainbowPickerDialog.this.dismiss();
			}
		});
	}
}
