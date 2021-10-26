package com.teamapricot.projectwalking.controller;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamapricot.projectwalking.model.Statistics;
import com.teamapricot.projectwalking.model.StatisticsListItemBinding;
import com.teamapricot.projectwalking.model.database.Achievement;
import com.teamapricot.projectwalking.model.AchievementListItemBinding;

import java.util.List;

/**
 * @author Erik Wahlberger
 * @version 2021-10-26
 *
 * {@link RecyclerView.Adapter} that can display an {@link Statistics}.
 */
public class StatisticsRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewAdapter.ViewHolder> {

    private final List<Statistics> mValues;

    /**
     * Creates a new StatisticsRecyclerViewAdapter for the specified items
     * @param items The items that will be shown in the {@link RecyclerView}
     */
    public StatisticsRecyclerViewAdapter(List<Statistics> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(StatisticsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mValueView.setText(mValues.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Helper class used to hold a reference to all {@link View}'s in the list item, as well as the data object that will be displayed in the corresponding {@link View}'s.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitleView;
        public final TextView mValueView;
        public Statistics mItem;

        public ViewHolder(StatisticsListItemBinding binding) {
            super(binding.getRoot());
            mTitleView = binding.title;
            mValueView = binding.value;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}