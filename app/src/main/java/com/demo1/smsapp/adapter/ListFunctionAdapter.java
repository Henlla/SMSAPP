package com.demo1.smsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ListFunctionBinding;
import com.demo1.smsapp.dto.FunctionModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListFunctionAdapter extends RecyclerView.Adapter<ListFunctionAdapter.ListFunctionVH>{

    List<FunctionModel> list;
    public void setData(List<FunctionModel> list){
        this.list = list;
    }

    Context context;

    public IClickItem item;
    public void ClickFunction(IClickItem item){
        this.item = item;
    }

    @NonNull
    @NotNull
    @Override
    public ListFunctionVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListFunctionBinding  listFunctionBinding = ListFunctionBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new ListFunctionVH(listFunctionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListFunctionVH holder, int position) {
        FunctionModel functionModel = list.get(position);
        holder.listFunctionBinding.title.setText(functionModel.getTitle());
        Glide.with(context)
                .load(functionModel.getImg())
                .centerCrop()
                .error(R.drawable.image_notavailable)
                .into(holder.listFunctionBinding.imgFunc);
        holder.listFunctionBinding.layout.setOnClickListener(view->{
            item.ClickFunction(functionModel.getCode());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListFunctionVH extends RecyclerView.ViewHolder{
        ListFunctionBinding listFunctionBinding;
        public ListFunctionVH(ListFunctionBinding listFunctionBinding) {
            super(listFunctionBinding.getRoot());
            this.listFunctionBinding = listFunctionBinding;
        }
    }

   public interface IClickItem{
       public void ClickFunction(String functionName);
    }
}
