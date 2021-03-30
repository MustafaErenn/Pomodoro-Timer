package com.mustafaeren.pomodorotimer;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.TimeHolder> {
    private ArrayList<FinishModel> finishModels;

    public DoneAdapter(ArrayList<FinishModel> finishModels) {
        this.finishModels = finishModels;
    }

    @NonNull
    @Override
    public TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_finishes,parent,false);

        return new DoneAdapter.TimeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeHolder holder, int position) {
        holder.id.setText("Number: "+finishModels.get(position).getId());
        holder.desc.setText("Description: "+finishModels.get(position).getDescription());
        holder.datetime.setText("Date: "+finishModels.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return finishModels.size();
    }

    public class TimeHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView desc;
        TextView datetime;
        public TimeHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.idText);
            desc = itemView.findViewById(R.id.descText);
            datetime = itemView.findViewById(R.id.dateTimeText);
        }
    }
}
