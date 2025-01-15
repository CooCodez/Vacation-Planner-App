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

import java.util.ArrayList;
import java.util.List;

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
            holder.startDateView.setText(current.getStartDate().toString());
            holder.endDateView.setText(current.getEndDate().toString());

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
        private final TextView excursionTitleView; // Add this line

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
