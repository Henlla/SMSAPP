package com.demo1.smsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListScheduleBinding;
import com.demo1.smsapp.dto.ScheduleDetailModel;
import com.demo1.smsapp.models.Schedule;
import com.demo1.smsapp.models.ScheduleDetail;
import com.demo1.smsapp.utils.ConvertDayOfWeek;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListScheduleAdapter extends RecyclerView.Adapter<ListScheduleAdapter.ListScheduleVH> {
    List<ScheduleDetailModel> scheduleDetails;
    public void setList(List<ScheduleDetailModel> scheduleDetails){
        this.scheduleDetails = scheduleDetails;
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public ListScheduleVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListScheduleBinding listScheduleBinding = ListScheduleBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ListScheduleVH(listScheduleBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListScheduleVH holder, int position) {
        ScheduleDetailModel scheduleDetail = scheduleDetails.get(position);
        String[] splitString = scheduleDetail.getDate().split("-");
        String dateFormat= splitString[2] +"/"+splitString[1]+"/"+splitString[0];
        String dayOfWeek = ConvertDayOfWeek.convertDayOfWeek(scheduleDetail.getDayOfWeek());
        holder.scheduleBinding.tvSubjectName.setText("Môn: "+scheduleDetail.getSubjectBySubjectId().getSubjectName());
        holder.scheduleBinding.tvTeacher.setText("Gv: "+scheduleDetail.getTeacherName());
        holder.scheduleBinding.tvClass.setText("Lớp : "+scheduleDetail.getClassName());
        holder.scheduleBinding.tvDayOfWeek.setText(dayOfWeek);
        holder.scheduleBinding.tvTime.setText(scheduleDetail.getTime());
        holder.scheduleBinding.tvDate.setText(dateFormat);
    }

    @Override
    public int getItemCount() {
        return scheduleDetails.isEmpty()? 0 : scheduleDetails.size();
    }

    public void synData(List<ScheduleDetailModel> oldList,List<ScheduleDetailModel> newList){
        DiffUtil.DiffResult result =  DiffUtil.calculateDiff(new ScheduleCallbackUtils(oldList,newList));
        result.dispatchUpdatesTo(this);
        oldList.clear();
        oldList.addAll(newList);
    }

    public static class ListScheduleVH extends RecyclerView.ViewHolder{
        ListScheduleBinding scheduleBinding;
        public ListScheduleVH(ListScheduleBinding scheduleBinding) {
            super(scheduleBinding.getRoot());
            this.scheduleBinding = scheduleBinding;
        }
    }
}
