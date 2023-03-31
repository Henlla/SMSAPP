package com.demo1.smsapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListClassAttendanceBinding;
import com.demo1.smsapp.dto.TakeAttendanceView;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ListTakeAttendanceAdapter extends RecyclerView.Adapter<ListTakeAttendanceAdapter.ViewHolder> {
    List<TakeAttendanceView> list;

    public void setData(List<TakeAttendanceView> list) {
        this.list = list;
    }

    String currentTime;
    OnItemClick onItemClick;

    public interface OnItemClick {
        void onClickItem(TakeAttendanceView takeAttendanceView);
    }

    public void onClick(OnItemClick itemClick) {
        this.onItemClick = itemClick;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ListClassAttendanceBinding binding = ListClassAttendanceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }
    @SuppressLint({"SetTextI18n","NewApi"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        TakeAttendanceView takeAttendanceView = list.get(position);
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        currentTime = (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
        holder.binding.date.setText(takeAttendanceView.getDate());
        holder.binding.slot.setText("Slot " + takeAttendanceView.getSlot());
        holder.binding.classes.setText(takeAttendanceView.getClass_code());
        holder.binding.startTime.setText(takeAttendanceView.getStartTime());
        holder.binding.endTime.setText(takeAttendanceView.getEndTime());
        holder.binding.subjectCode.setText(takeAttendanceView.getSubject_code());
        holder.binding.subjectName.setText(takeAttendanceView.getSubject_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onClickItem(takeAttendanceView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListClassAttendanceBinding binding;

        public ViewHolder(@NonNull ListClassAttendanceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
