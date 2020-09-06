package com.alfatraining.beispiel.app.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alfatraining.beispiel.app.R;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class ListAdapterAndroidVersions extends BaseAdapter {

    Context mContext;
    private final List<String> mAndroidVersionsList;

    public ListAdapterAndroidVersions(@NonNull Context context) {
        mContext = context;
        mAndroidVersionsList = Arrays.asList(
                context.getResources().getStringArray(
                        R.array.android_versions_array
                )
        );
    }
    @Override
    public int getCount() {
        return mAndroidVersionsList.size();
    }

    @Override
    public String getItem(int position) {
        return mAndroidVersionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_row, parent, false);
        }

        // get the TextView for version name and version number
        TextView textViewVersionName =
                convertView.findViewById(R.id.txt_version_name);
        TextView textViewVersionNumber =
                convertView.findViewById(R.id.txt_version_num);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(position * 500);
                } catch(InterruptedException e) {
                    //TODO
                }
            }
        });

        //sets the text for item name and item description from the current item object
        textViewVersionName.setText(getItem(position));
        textViewVersionNumber.setText(mContext.getString (R.string.android_ver, position));

        // returns the view for the current row
        return convertView;    }
}