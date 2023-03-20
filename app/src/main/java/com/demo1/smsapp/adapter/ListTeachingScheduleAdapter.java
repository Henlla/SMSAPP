package com.demo1.smsapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListTeachingScheduleBinding;
import com.demo1.smsapp.dto.ScheduleGroupDTO;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListTeachingScheduleAdapter extends RecyclerView.Adapter<ListTeachingScheduleAdapter.TeachingScheduleVH> {

    List<ScheduleGroupDTO> scheduleGroupDTOS;
    ListTeachingScheduleDetailsAdapter adapter;
    Context context;

    public void setData(List<ScheduleGroupDTO> scheduleGroupDTOS){
        this.scheduleGroupDTOS = scheduleGroupDTOS;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public TeachingScheduleVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListTeachingScheduleBinding binding = ListTeachingScheduleBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        adapter= new ListTeachingScheduleDetailsAdapter();
        context= parent.getContext();
        return new TeachingScheduleVH(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull TeachingScheduleVH holder, int position) {
        ScheduleGroupDTO scheduleGroupDTO = scheduleGroupDTOS.get(position);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("LLLL dd , yyyy");
        holder.binding.date.setText(scheduleGroupDTO.getDate().format(dateTimeFormatter));
        adapter.setData(scheduleGroupDTO.getList());
        holder.binding.rcvTeachingScheduleDetails.setAdapter(adapter);
        holder.binding.rcvTeachingScheduleDetails.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return scheduleGroupDTOS.isEmpty() ? 0 : scheduleGroupDTOS.size();
    }

    public static class TeachingScheduleVH extends RecyclerView.ViewHolder{
        ListTeachingScheduleBinding binding;
        public TeachingScheduleVH(ListTeachingScheduleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
