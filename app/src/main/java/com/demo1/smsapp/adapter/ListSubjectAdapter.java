package com.demo1.smsapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.databinding.ListSubjectBinding;
import com.demo1.smsapp.dto.SubjectView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListSubjectAdapter extends RecyclerView.Adapter<ListSubjectAdapter.ViewHolder> {

    List<SubjectView> listSubject;

    public void setData(List<SubjectView> listSubject) {
        this.listSubject = listSubject;
    }

    ItemClick itemClick;

    public interface ItemClick {
        void OnClickItem(SubjectView subjectView);
    }

    public void OnItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ListSubjectBinding binding = ListSubjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        SubjectView subject = listSubject.get(position);
        if (Integer.parseInt(subject.getStatus()) >= 0 && Integer.parseInt(subject.getStatus()) <= 10) {
            holder.binding.subjectStatus.setTextColor(Color.BLUE);
        } else if (Integer.parseInt(subject.getStatus()) >= 10 && Integer.parseInt(subject.getStatus()) <= 20) {
            holder.binding.subjectStatus.setTextColor(Color.BLUE);
        } else {
            holder.binding.subjectStatus.setTextColor(Color.RED);

        }
        holder.binding.subjectCode.setText(subject.getSubject_code());
        holder.binding.subjectName.setText(subject.getSubject_name());
        holder.binding.subjectStatus.setText(subject.getStatus() + "%");
        holder.itemView.setOnClickListener(v -> {
            itemClick.OnClickItem(subject);
        });
    }

    @Override
    public int getItemCount() {
        return listSubject.size() == 0 ? 0 : listSubject.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListSubjectBinding binding;

        public ViewHolder(@NotNull ListSubjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
