package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Retrofit.Model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends ArrayAdapter<Step> {


    @BindView(R.id.step_id)
    TextView step_id_tv;
    @BindView(R.id.step_short_description)
    TextView step_short_description_tv;

    public StepAdapter(@NonNull Context context, int resource, @NonNull List<Step> msteps) {
        super(context, resource, msteps);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Context mContext = parent.getContext();
        Step step = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.step_card, parent, false);
        }

        ButterKnife.bind(this, convertView);

        int id_nr = step.getId();
        int id_real = id_nr + 1;
        String id = String.valueOf(id_real);
        step_id_tv.setText(id);
        String shortDescription = step.getShortDescription();
        step_short_description_tv.setText(shortDescription);
        return convertView;
    }

}

