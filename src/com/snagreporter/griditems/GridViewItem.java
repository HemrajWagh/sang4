package com.snagreporter.griditems;

import android.view.LayoutInflater;
import android.view.View;

public interface GridViewItem {
	public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
