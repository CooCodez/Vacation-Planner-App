package com.example.d308vacationplanner.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Excursion;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public ExcursionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView2;
        private final TextView excursionItemView3;

        public ExcursionViewHolder(@NonNull View itemView) { // Add @NonNull here
            super(itemView);
            excursionItemView2 = itemView.findViewById(R.id.textView2); // Excursion Name
            excursionItemView3 = itemView.findViewById(R.id.textView3); // Excursion Date

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                final Excursion current = mExcursions.get(position);
                Intent intent = new Intent(context, ExcursionDetails.class);
                intent.putExtra("id", current.getExcursionID());
                intent.putExtra("name", current.getExcursionName());
                intent.putExtra("hotel", current.getHotel());
                intent.putExtra("vacationID", current.getVacationID());
                intent.putExtra("date", current.getExcursionDate() != null ? current.getExcursionDate().getTime() : -1);
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (mExcursions != null) {
            Excursion current = mExcursions.get(position);
            holder.excursionItemView2.setText(current.getExcursionName());
            holder.excursionItemView2.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.excursionItemView2.setTextSize(20);
            holder.excursionItemView2.setTypeface(null, Typeface.BOLD);

            if (current.getExcursionDate() != null) {
                holder.excursionItemView3.setText(dateFormat.format(current.getExcursionDate()));
            } else {
                holder.excursionItemView3.setText(context.getString(R.string.date_not_set)); // Use the string resource
            }
            holder.excursionItemView3.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.excursionItemView3.setTextSize(20); // Adjust text size
            holder.excursionItemView3.setTypeface(null, Typeface.BOLD);

            // Apply alternating gradient background to each row
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.drawable.gradient_background_even); // Even rows: Gradient background
            } else {
                holder.itemView.setBackgroundResource(R.drawable.gradient_background_odd); // Odd rows: Alternate gradient background
            }
        } else {
            holder.excursionItemView2.setText(context.getString(R.string.no_excursion_name));
            holder.excursionItemView3.setText(context.getString(R.string.no_date_info));
        }
    }


    @Override
    public int getItemCount() {
        return (mExcursions != null) ? mExcursions.size() : 0;
    }

    public void setExcursions(List<Excursion> excursions) {
        mExcursions = excursions;
        notifyDataSetChanged();
    }
}
