package com.snagreporter.griditems;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class GridArrayAdapter extends ArrayAdapter<GridViewItem> {
	private LayoutInflater mInflater;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    private List<GridViewItem> items;

    public GridArrayAdapter(Context context, List<GridViewItem> items) {
        super(context, 0, items);
        this.items = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).getView(mInflater, convertView);
    }
}
