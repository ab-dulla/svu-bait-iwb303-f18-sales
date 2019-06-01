package org.svuonline.f18sales.salesmen.management;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.svuonline.f18sales.R;
import org.svuonline.f18sales.model.Region;

import java.util.ArrayList;

public class RegionSpinnerArrayAdapter extends ArrayAdapter<Region> {

    public RegionSpinnerArrayAdapter(Context context, ArrayList<Region> regionsList) {
        super(context, 0, regionsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return view(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return view(position, convertView, parent);
    }

    private View view(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
        }
        TextView regionTextView = convertView.findViewById(android.R.id.text1);
        Region region = getItem(position);
        regionTextView.setText(region.getName());
        return convertView;
    }
}
