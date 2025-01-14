package com.example.d308vacationplanner.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Vacation;

import java.util.ArrayList;
import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> implements Filterable {
    private List<Vacation> mVacations;
    private List<Vacation> mVacationsFull;
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
            vacationLocationView = itemView.findViewById(R.id.vacationLocation);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Vacation current = mVacations.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);
                    intent.putExtra("id", current.getVacationID());
                    intent.putExtra("name", current.getVacationName());
                    intent.putExtra("hotel", current.getHotel());
                    intent.putExtra("startDate", current.getStartDate().getTime());
                    intent.putExtra("endDate", current.getEndDate().getTime());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (mVacations != null) {
            Vacation current = mVacations.get(position);
            holder.vacationLocationView.setText(current.getVacationName());
            holder.vacationLocationView.setTextSize(30);
            holder.vacationLocationView.setTypeface(null, Typeface.BOLD);
            holder.vacationLocationView.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.vacationLocationView.setBackgroundResource(position % 2 == 0 ? R.drawable.gradient_background_even : R.drawable.gradient_background_odd);
        } else {
            holder.vacationLocationView.setText("No Vacation Name");
        }
    }

    @Override
    public int getItemCount() {
        return mVacations != null ? mVacations.size() : 0;
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        mVacationsFull = new ArrayList<>(vacations);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return vacationFilter;
    }

    private final Filter vacationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Vacation> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mVacationsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Vacation vacation : mVacationsFull) {
                    if (vacation.getVacationName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(vacation);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mVacations.clear();
            mVacations.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
