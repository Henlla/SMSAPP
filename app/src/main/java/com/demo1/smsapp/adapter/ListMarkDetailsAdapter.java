package com.demo1.smsapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ListMarkDetailsBinding;
import com.demo1.smsapp.dto.MarkModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListMarkDetailsAdapter extends RecyclerView.Adapter<ListMarkDetailsAdapter.MarkViewHolder> {
    List<MarkModel> markModelList;

    public void setList(List<MarkModel> markModelList) {
        this.markModelList = markModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public MarkViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListMarkDetailsBinding listMarkDetailsBinding = ListMarkDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MarkViewHolder(listMarkDetailsBinding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MarkViewHolder holder, int position) {
        MarkModel model = markModelList.get(position);
        if(model.getAsm() > 0.0){
            holder.listMarkDetailsBinding.asm.setText("ASM: "+model.getAsm().toString());
        }else{
            holder.listMarkDetailsBinding.asm.setText("");
        }

        if(model.getObj() > 0.0){
            holder.listMarkDetailsBinding.obj.setText("OBJ: "+model.getObj().toString());
        }else{
            holder.listMarkDetailsBinding.obj.setText("");
        }

        if(model.getObj() == 0.0 && model.getAsm() == 0.0){
            holder.listMarkDetailsBinding.status.setText("Not started");
            holder.listMarkDetailsBinding.status.setTextColor(R.color.red);
        }else{
            if((model.getObj() + model.getAsm())/2 > 40.0){
                holder.listMarkDetailsBinding.status.setText("PASS");
                holder.listMarkDetailsBinding.status.setTextColor(R.color.green);
            }else{
                holder.listMarkDetailsBinding.status.setText("Not pass");
                holder.listMarkDetailsBinding.status.setTextColor(R.color.red);
            }
        }
        holder.listMarkDetailsBinding.subCode.setText(model.getSubject().getSubjectCode());
        holder.listMarkDetailsBinding.subName.setText(model.getSubject().getSubjectName());

    }

    @Override
    public int getItemCount() {
        return markModelList.size() > 0 ? markModelList.size() : 0;
    }

    public static class MarkViewHolder extends RecyclerView.ViewHolder {
        ListMarkDetailsBinding listMarkDetailsBinding;

        public MarkViewHolder(ListMarkDetailsBinding listMarkDetailsBinding) {
            super(listMarkDetailsBinding.getRoot());
            this.listMarkDetailsBinding = listMarkDetailsBinding;
        }
    }
}
