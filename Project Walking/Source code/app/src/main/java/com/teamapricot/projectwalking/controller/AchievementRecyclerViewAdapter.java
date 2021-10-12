package com.teamapricot.projectwalking.controller;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamapricot.projectwalking.model.database.Achievement;
import com.teamapricot.projectwalking.model.AchievementListItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display an {@link Achievement}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AchievementRecyclerViewAdapter extends RecyclerView.Adapter<AchievementRecyclerViewAdapter.ViewHolder> {

    private final List<Achievement> mValues;

    public AchievementRecyclerViewAdapter(List<Achievement> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(AchievementListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getTitle());
        holder.mPointsView.setText("" + mValues.get(position).getPoints());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public final TextView mPointsView;
        public Achievement mItem;

        public ViewHolder(AchievementListItemBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
            mPointsView = binding.points;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}