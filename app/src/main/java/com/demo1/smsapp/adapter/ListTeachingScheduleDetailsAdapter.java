package com.demo1.smsapp.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListTeachingScheduleBinding;
import com.demo1.smsapp.databinding.ListTeachingScheduleDetailsBinding;
import com.demo1.smsapp.dto.ScheduleDetailModel;
import com.demo1.smsapp.dto.ScheduleGroupDTO;
import com.demo1.smsapp.dto.TeachingScheduleModel;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class ListTeachingScheduleDetailsAdapter extends RecyclerView.Adapter<ListTeachingScheduleDetailsAdapter.TeachingScheduleDetailsVH> {

    List<TeachingScheduleModel> scheduleDetailModels;

    public void setData(List<TeachingScheduleModel> scheduleDetailModels){
        this.scheduleDetailModels = scheduleDetailModels;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public TeachingScheduleDetailsVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListTeachingScheduleDetailsBinding binding = ListTeachingScheduleDetailsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new TeachingScheduleDetailsVH(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull TeachingScheduleDetailsVH holder, int position) {
        TeachingScheduleModel scheduleDetailModel = scheduleDetailModels.get(position);
        LocalDate date = LocalDate.parse(scheduleDetailModel.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        holder.binding.tvClass.setText("Class: "+scheduleDetailModel.getClassCode());
        holder.binding.dayOfWeek.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US).toString());
        holder.binding.date.setText(date.format(formatter));
        holder.binding.tvSubName.setText(scheduleDetailModel.getSubject().getSubjectName());
        holder.binding.tvRoom.setText("Room: "+scheduleDetailModel.getDepartmentCode()+"_"+scheduleDetailModel.getRoomCode());
        if(scheduleDetailModel.getShift().charAt(0) == 'M'){
            if(scheduleDetailModel.getSlot().equals(1)){
                holder.binding.tvTime.setText("Time: 8:00 - 10:00");
            }else{
                holder.binding.tvTime.setText("Time: 10:00 - 12:00");
            }
        }else if (scheduleDetailModel.getShift().charAt(0) == 'A'){
            if(scheduleDetailModel.getSlot().equals(1)){
                holder.binding.tvTime.setText("Time: 12:30 - 15:30");
            }else{
                holder.binding.tvTime.setText("Time: 15:30 - 17:30");
            }
        }else{
            if(scheduleDetailModel.getSlot().equals(1)){
                holder.binding.tvTime.setText("Time: 17:30 - 19:30");
            }else{
                holder.binding.tvTime.setText("Time: 19:30 - 21:30");
            }
        }
    }

    @Override
    public int getItemCount() {
        return scheduleDetailModels.isEmpty() ? 0 : scheduleDetailModels.size();
    }

    public static class TeachingScheduleDetailsVH extends RecyclerView.ViewHolder{
        ListTeachingScheduleDetailsBinding binding;
        public TeachingScheduleDetailsVH(ListTeachingScheduleDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
