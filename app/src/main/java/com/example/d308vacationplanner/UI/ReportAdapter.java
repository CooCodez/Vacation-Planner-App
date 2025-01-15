package com.example.d308vacationplanner.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Vacation;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Vacation> mVacations;
    private final LayoutInflater mInflater;

    public ReportAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
        }
    }

    @Override
    public int getItemCount() {
        return (mVacations != null) ? mVacations.size() : 0;
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        notifyDataSetChanged();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationNameView;
        private final TextView hotelView;
        private final TextView startDateView;
        private final TextView endDateView;

        public ReportViewHolder(View itemView) {
            super(itemView);
            vacationNameView = itemView.findViewById(R.id.vacationName);
            hotelView = itemView.findViewById(R.id.hotel);
            startDateView = itemView.findViewById(R.id.startDate);
            endDateView = itemView.findViewById(R.id.endDate);
        }
    }
}
