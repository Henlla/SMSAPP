package com.demo1.smsapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListAttendanceDetailBinding;
import com.demo1.smsapp.dto.AttendanceDetailView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListAttendanceDetailAdapter extends RecyclerView.Adapter<ListAttendanceDetailAdapter.ViewHolder> {
    List<AttendanceDetailView> list;

    public void setData(List<AttendanceDetailView> list) {
        this.list = list;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ListAttendanceDetailBinding binding = ListAttendanceDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        AttendanceDetailView attendanceView = list.get(position);
        holder.binding.slot.setText("Slot " + attendanceView.getSlot().toString());
        holder.binding.teacher.setText(attendanceView.getTeacher_name());
        if (attendanceView.getStatus().equals("1")) {
            holder.binding.status.setText("PRESENT");
            holder.binding.status.setTextColor(Color.GREEN);
        } else if (attendanceView.getStatus().equals("0")) {
            holder.binding.status.setText("ABSENT");
            holder.binding.status.setTextColor(Color.RED);
        } else {
            holder.binding.status.setText("FUTURE");
            holder.binding.status.setTextColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListAttendanceDetailBinding binding;

        public ViewHolder(@NonNull ListAttendanceDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
