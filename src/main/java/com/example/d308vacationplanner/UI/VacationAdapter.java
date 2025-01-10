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
import com.example.d308vacationplanner.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationLocationView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationLocationView = itemView.findViewById(R.id.vacationLocation);  // Ensure you use the correct ID from XML

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                final Vacation current = mVacations.get(position);
                Intent intent = new Intent(context, VacationDetails.class);
                intent.putExtra("id", current.getVacationID());
                intent.putExtra("name", current.getVacationName());
                intent.putExtra("hotel", current.getHotel());
                intent.putExtra("startDate", current.getStartDate().getTime());  // Convert Date to long
                intent.putExtra("endDate", current.getEndDate().getTime());  // Convert Date to long
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list, parent, false);  // Use the updated XML layout
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if (mVacations != null) {
            Vacation current = mVacations.get(position);
            String name = current.getVacationName();
            holder.vacationLocationView.setText(name);

            // Set the text size, font weight, and style (bold)
            holder.vacationLocationView.setTextSize(30);  // Increase font size
            holder.vacationLocationView.setTypeface(null, Typeface.BOLD);  // Make text bold

            // Set the text color to white for all rows
            holder.vacationLocationView.setTextColor(ContextCompat.getColor(context, R.color.white));

            // Apply alternating gradient background with borders to each row
            if (position % 2 == 0) {
                holder.vacationLocationView.setBackgroundResource(R.drawable.gradient_background_even); // Even rows: Gradient background with border
            } else {
                holder.vacationLocationView.setBackgroundResource(R.drawable.gradient_background_odd); // Odd rows: Alternating gradient background with border
            }
        } else {
            holder.vacationLocationView.setText(context.getString(R.string.no_vacation_name));
        }
    }



    @Override
    public int getItemCount() {
        return mVacations != null ? mVacations.size() : 0;
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        notifyDataSetChanged();
    }
}
