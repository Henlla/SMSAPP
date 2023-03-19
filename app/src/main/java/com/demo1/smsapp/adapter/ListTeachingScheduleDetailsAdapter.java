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
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class ListTeachingScheduleDetailsAdapter extends RecyclerView.Adapter<ListTeachingScheduleDetailsAdapter.TeachingScheduleDetailsVH> {

    List<ScheduleDetailModel> scheduleDetailModels;

    public void setData(List<ScheduleDetailModel> scheduleDetailModels){
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
        ScheduleDetailModel scheduleDetailModel = scheduleDetailModels.get(position);
        LocalDate date = LocalDate.parse(scheduleDetailModel.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        holder.binding.tvClass.setText("Lớp : "+scheduleDetailModel.getClassName());
        holder.binding.dayOfWeek.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US).toString());
        holder.binding.date.setText(date.format(formatter));
        holder.binding.tvSubName.setText(scheduleDetailModel.getSubjectBySubjectId().getSubjectName());
        holder.binding.tvTime.setText("Thời gian : "+scheduleDetailModel.getTimeStart()+" - "+scheduleDetailModel.getTimeEnd());


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
