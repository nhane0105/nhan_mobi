package com.example.r_api_bai1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class CountryAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<Country> countries;

    public CountryAdapter(Context context, ArrayList<Country> countries) {
        this.context = context;
        this.countries = countries != null ? countries : new ArrayList<>();
    }

    public void updateData(ArrayList<Country> newCountries) {
        this.countries = newCountries != null ? newCountries : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Country country = countries.get(position);

        holder.tvName.setText(country.getName());
        holder.tvDetails.setText("Capital: " + country.getCapital() + " • Region: " + country.getRegion());

        String flagUrl = country.getFlagUrl();
        if (flagUrl != null && !flagUrl.isEmpty()) {
            Glide.with(context)
                    .load(flagUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imgFlag);
        } else {
            holder.imgFlag.setImageDrawable(null);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgFlag;
        TextView tvName;
        TextView tvDetails;

        ViewHolder(View view) {
            imgFlag = view.findViewById(R.id.imgFlag);
            tvName = view.findViewById(R.id.tvName);
            tvDetails = view.findViewById(R.id.tvDetails);
        }
    }
}
