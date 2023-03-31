package com.demo1.smsapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ListScheduleDetailsBinding;
import com.demo1.smsapp.dto.ScheduleDetailModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListScheduleDetailsAdapter extends RecyclerView.Adapter<ListScheduleDetailsAdapter.ScheduleDetailsVH> {

    List<ScheduleDetailModel> scheduleDetailModels;
    public void setData(List<ScheduleDetailModel> scheduleDetailModels){
        this.scheduleDetailModels = scheduleDetailModels;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ScheduleDetailsVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListScheduleDetailsBinding scheduleDetailsBinding = ListScheduleDetailsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ScheduleDetailsVH(scheduleDetailsBinding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ScheduleDetailsVH holder, int position) {
            ScheduleDetailModel scheduleDetailModel = scheduleDetailModels.get(position);
            if(scheduleDetailModel.getSlot().equals(1)){
                holder.scheduleDetailsBinding.slot.setText("Slot 1");
                holder.scheduleDetailsBinding.imgLine.setImageResource(R.drawable.line_start);
            }else{
                holder.scheduleDetailsBinding.slot.setText("Slot 2");
                holder.scheduleDetailsBinding.slot.setTextColor(R.color.green);
                holder.scheduleDetailsBinding.subTitle.setTextColor(R.color.green);
                holder.scheduleDetailsBinding.roomCode.setTextColor(R.color.green);
                holder.scheduleDetailsBinding.imgLine.setImageResource(R.drawable.line_end);
            }
            holder.scheduleDetailsBinding.time1.setText(scheduleDetailModel.getTimeStart());
            holder.scheduleDetailsBinding.time2.setText(scheduleDetailModel.getTimeEnd());
            holder.scheduleDetailsBinding.subTitle.setText(scheduleDetailModel.getSubjectBySubjectId().getSubjectName());

            holder.scheduleDetailsBinding.roomCode.setText("Room "+scheduleDetailModel.getDepartmentCode()+"_"+scheduleDetailModel.getRoomCode());
            holder.scheduleDetailsBinding.teacher.setText("GV:"+scheduleDetailModel.getTeacherName());
            holder.scheduleDetailsBinding.tvClass.setText("Class: "+scheduleDetailModel.getClassName());
    }

    @Override
    public int getItemCount() {
        return scheduleDetailModels.size() == 0 ? 0 :scheduleDetailModels.size();
    }

    public static class ScheduleDetailsVH extends RecyclerView.ViewHolder{
        ListScheduleDetailsBinding scheduleDetailsBinding;
        public ScheduleDetailsVH( ListScheduleDetailsBinding scheduleDetailsBinding) {
            super(scheduleDetailsBinding.getRoot());
            this.scheduleDetailsBinding = scheduleDetailsBinding;
        }
    }
}
