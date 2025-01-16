package com.example.d308vacationplanner.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Vacation> mVacations;
    private final LayoutInflater mInflater;
    private List<Excursion> mExcursions;  // Store excursions to display

    public ReportAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mVacations = new ArrayList<>();
        mExcursions = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        if (mVacations != null) {
            Vacation current = mVacations.get(position);

            holder.vacationNameView.setText(current.getVacationName());
            holder.hotelView.setText(current.getHotel());

            // Format the start and end dates to a 12-hour format with AM/PM
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault()); // Using "h" instead of "hh"
            String formattedStartDate = sdf.format(current.getStartDate());
            String formattedEndDate = sdf.format(current.getEndDate());

            holder.startDateView.setText(formattedStartDate);
            holder.endDateView.setText(formattedEndDate);

            // Display the "Excursions" label
            holder.excursionTitleView.setText("Excursions");

            // Display excursions for this vacation
            StringBuilder excursions = new StringBuilder();
            for (Excursion excursion : mExcursions) {
                if (excursion.getVacationID() == current.getVacationID()) {
                    excursions.append(excursion.getExcursionName()).append("\n");
                }
            }
            holder.excursionView.setText(excursions.toString());
        }
    }


    @Override
    public int getItemCount() {
        return (mVacations != null) ? mVacations.size() : 0;
    }

    // Method to set the vacations list and excursions
    public void setVacations(List<Vacation> vacations, List<Excursion> excursions) {
        mVacations = vacations;
        mExcursions = excursions;
        notifyDataSetChanged();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationNameView;
        private final TextView hotelView;
        private final TextView startDateView;
        private final TextView endDateView;
        private final TextView excursionView;
        private final TextView excursionTitleView;

        public ReportViewHolder(View itemView) {
            super(itemView);
            vacationNameView = itemView.findViewById(R.id.vacationName);
            hotelView = itemView.findViewById(R.id.hotel);
            startDateView = itemView.findViewById(R.id.startDate);
            endDateView = itemView.findViewById(R.id.endDate);
            excursionView = itemView.findViewById(R.id.excursionList);
            excursionTitleView = itemView.findViewById(R.id.excursionTitle); // Bind the title TextView
        }
    }
}
