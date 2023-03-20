package com.demo1.smsapp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListAttendanceBinding;
import com.demo1.smsapp.dto.AttendanceView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListAttendanceAdapter extends RecyclerView.Adapter<ListAttendanceAdapter.ViewHolder> {
    List<AttendanceView> listAttendance;

    public void setData(List<AttendanceView> listAttendance) {
        this.listAttendance = listAttendance;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ListAttendanceBinding binding = ListAttendanceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        AttendanceView attendance = listAttendance.get(position);
        if(attendance.getStatus().equals("0")){
            holder.binding.attendanceStatus.setTextColor(Color.RED);
            holder.binding.attendanceStatus.setText("ABSENT");
        }else if(attendance.getStatus().equals("1")){
            holder.binding.attendanceStatus.setTextColor(Color.GREEN);
            holder.binding.attendanceStatus.setText("PRESENT");
        }else{
            holder.binding.attendanceStatus.setTextColor(Color.BLACK);
            holder.binding.attendanceStatus.setText("FUTURE");
        }
        holder.binding.attendanceSlot.setText(String.valueOf("Slot " + attendance.getSlot()));
        holder.binding.attendanceClass.setText(attendance.getClass_code());
        holder.binding.attendanceDate.setText(attendance.getDate());
        holder.binding.attendanceTeacher.setText(attendance.getTeacher_name());
    }

    @Override
    public int getItemCount() {
        return listAttendance.size() == 0 ? 0 : listAttendance.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListAttendanceBinding binding;

        public ViewHolder(@NotNull ListAttendanceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
