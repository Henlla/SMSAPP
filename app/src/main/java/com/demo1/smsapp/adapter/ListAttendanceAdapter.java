package com.demo1.smsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListAttendanceBinding;
import com.demo1.smsapp.dto.AttendanceView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListAttendanceAdapter extends RecyclerView.Adapter<ListAttendanceAdapter.ViewHolder> {
    List<AttendanceView> listAttendanceView;

    ListAttendanceDetailAdapter attendanceDetailAdapter;

    Context context;

    public void setData(List<AttendanceView> listAttendanceView) {
        this.listAttendanceView = listAttendanceView;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ListAttendanceBinding binding = ListAttendanceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        AttendanceView attendance = listAttendanceView.get(position);
        attendanceDetailAdapter = new ListAttendanceDetailAdapter();
        holder.binding.attendanceDetailRcv.setLayoutManager(new LinearLayoutManager(context));
        attendanceDetailAdapter.setData(attendance.getListAttendanceDetailView());
        holder.binding.attendanceDetailRcv.setAdapter(attendanceDetailAdapter);
        holder.binding.attendanceClass.setText(attendance.getClassCode());
        holder.binding.attendanceDate.setText(attendance.getDate());
    }

    @Override
    public int getItemCount() {
        return listAttendanceView.size() == 0 ? 0 : listAttendanceView.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListAttendanceBinding binding;

        public ViewHolder(@NotNull ListAttendanceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
