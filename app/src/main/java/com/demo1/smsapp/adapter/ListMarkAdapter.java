package com.demo1.smsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ListMarkBinding;
import com.demo1.smsapp.databinding.ListMarkDetailsBinding;
import com.demo1.smsapp.dto.MarkGroupModel;
import com.demo1.smsapp.dto.MarkModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListMarkAdapter extends RecyclerView.Adapter<ListMarkAdapter.ListMarkVH> {

    List<MarkGroupModel> markGroupModels;
    ListMarkDetailsAdapter detailsAdapter;
    Context context;

    public void setData(List<MarkGroupModel> markGroupModels){
        this.markGroupModels = markGroupModels;
    }

    @NonNull
    @NotNull
    @Override
    public ListMarkVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListMarkBinding listMarkBinding = ListMarkBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        detailsAdapter = new ListMarkDetailsAdapter();
        context = parent.getContext();
        return new ListMarkVH(listMarkBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListMarkVH holder, int position) {
            MarkGroupModel model = markGroupModels.get(position);
            holder.listMarkBinding.semester.setText("SEMESTER "+model.getSemester());
            detailsAdapter.setList(model.getList());
            holder.listMarkBinding.rcvListMarkDetails.setAdapter(detailsAdapter);
            holder.listMarkBinding.rcvListMarkDetails.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return markGroupModels.size() > 0 ? markGroupModels.size():0;
    }

    public static class ListMarkVH extends RecyclerView.ViewHolder {
        ListMarkBinding listMarkBinding;
        public ListMarkVH(ListMarkBinding listMarkBinding) {
            super(listMarkBinding.getRoot());
            this.listMarkBinding= listMarkBinding;
        }
    }
}
