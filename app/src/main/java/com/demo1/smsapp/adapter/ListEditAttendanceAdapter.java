package com.demo1.smsapp.adapter;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListStudentAttendanceBinding;
import com.demo1.smsapp.dto.StudentAttendanceView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListEditAttendanceAdapter extends RecyclerView.Adapter<ListEditAttendanceAdapter.ViewHolder> {
    List<StudentAttendanceView> list;

    public void setData(List<StudentAttendanceView> listStudent) {
        this.list = listStudent;
    }

    public List<StudentAttendanceView> getData() {
        return this.list;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ListStudentAttendanceBinding binding = ListStudentAttendanceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        StudentAttendanceView studentView = list.get(position);
        holder.binding.studentName.setText(studentView.getStudentName());
        holder.binding.edNote.getEditText().setText(studentView.getNote());
        if (studentView.isPresent() == 1) {
            holder.binding.switchAttendance.setChecked(true);
        } else {
            holder.binding.switchAttendance.setChecked(false);
        }
        holder.binding.switchAttendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.binding.switchAttendance.isChecked()) {
                    list.get(position).setPresent(1);
                } else {
                    list.get(position).setPresent(0);
                }
            }
        });

        holder.binding.edNote.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.get(position).setNote(holder.binding.edNote.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListStudentAttendanceBinding binding;

        public ViewHolder(@NonNull ListStudentAttendanceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
