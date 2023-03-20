package com.demo1.smsapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListApplicationBinding;
import com.demo1.smsapp.models.Application;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListApplicationAdapter extends RecyclerView.Adapter<ListApplicationAdapter.ViewHolder> {
    List<Application> listApplication;

    public void setData(List<Application> listApplication) {
        this.listApplication = listApplication;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListApplicationBinding binding = ListApplicationBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Application application = listApplication.get(position);
        if(application.getStatus().equals("PENDING")){
            holder.binding.appStatus.setTextColor(Color.rgb(255,252,0));
        }else if(application.getStatus().equals("APPROVED")){
            holder.binding.appStatus.setTextColor(Color.GREEN);
        }else{
            holder.binding.appStatus.setTextColor(Color.RED);
        }
        holder.binding.appDate.setText(application.getSendDate());
        holder.binding.appStatus.setText(application.getStatus());
        holder.binding.responseMessage.setText(application.getResponseNote());
        holder.binding.responseDate.setText(application.getResponseDate());
    }

    @Override
    public int getItemCount() {
        return listApplication.size() == 0 ? 0 : listApplication.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListApplicationBinding binding;
        public ViewHolder(@NonNull @NotNull ListApplicationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
