package com.demo1.smsapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListScheduleBinding;
import com.demo1.smsapp.dto.ScheduleDetailModel;
import com.demo1.smsapp.dto.ScheduleGroupDTO;
import com.demo1.smsapp.models.Schedule;
import com.demo1.smsapp.models.ScheduleDetail;
import com.demo1.smsapp.utils.ConvertDayOfWeek;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListScheduleAdapter extends RecyclerView.Adapter<ListScheduleAdapter.ListScheduleVH> {
    List<ScheduleGroupDTO> scheduleDetails;
    public void setList(List<ScheduleGroupDTO> scheduleDetails){
        this.scheduleDetails = scheduleDetails;
        notifyDataSetChanged();
    }
    ListScheduleDetailsAdapter listScheduleDetailsAdapter;
    Context context;
    @NonNull
    @NotNull
    @Override
    public ListScheduleVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListScheduleBinding listScheduleBinding = ListScheduleBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        listScheduleDetailsAdapter = new ListScheduleDetailsAdapter();
        context = parent.getContext();
        return new ListScheduleVH(listScheduleBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ListScheduleVH holder, int position) {
        ScheduleGroupDTO scheduleDetail = scheduleDetails.get(position);
        String[] splitString = scheduleDetail.getDate().toString().split("-");
        String dateFormat= splitString[2]+"/"+splitString[1];
        String dayOfWeek = ConvertDayOfWeek.convertDayOfWeek(scheduleDetail.getDate().getDayOfWeek().toString());
        holder.scheduleBinding.tvDayOfWeek.setText(dayOfWeek);
        holder.scheduleBinding.tvDate.setText(dateFormat);
        listScheduleDetailsAdapter.setData(scheduleDetail.getList());
        holder.scheduleBinding.rcvScheduleDetails.setAdapter(listScheduleDetailsAdapter);
        holder.scheduleBinding.rcvScheduleDetails.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        holder.scheduleBinding.rcvScheduleDetails.addItemDecoration(itemDecorator);
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
