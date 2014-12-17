package com.marchah.onedayonepic.tools;

import java.util.List;

import com.marchah.onedayonepic.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> listItem;
    private Typeface customFont;
    private Context context;

    public CustomAdapter(Context context, List<String> listItem, Typeface customFont) {
        mInflater = LayoutInflater.from(context);
        this.listItem = listItem;
        this.context = context;
        this.customFont = customFont;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
    	if (position < 0 || position >= listItem.size())
    		return null;
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
    	if (position < 0 || position >= listItem.size())
    		return -1;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null)
            v = mInflater.inflate(R.layout.spinner_style, null);
        TextView item = (TextView) v.findViewById(R.id.itemCategorieSpr);
        if (item != null) {
        	if (customFont != null)
        		item.setTypeface(customFont);
        	if (listItem.get(position).length() > 0)
        		item.setText(listItem.get(position).substring(0, 1).toUpperCase() + listItem.get(position).substring(1).toLowerCase());
        	item.setTextColor(context.getResources().getColor(R.color.text_color));
        }
        return v;
    }

}