package com.example.doctorregistration.Patient.Backend;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.doctorregistration.R;

import java.util.List;

public class CheckboxAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;

    public CheckboxAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.checkbox_item, null);
        }

        CheckBox checkBoxItem = itemView.findViewById(R.id.checkBoxItem);
        String itemText = data.get(position);

        checkBoxItem.setText(itemText);

        return itemView;
    }
}