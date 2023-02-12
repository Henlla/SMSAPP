package com.demo1.smsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ListNewsBinding;
import com.demo1.smsapp.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.NewViewHolder>{

    private List<News> listNews;

    private FirebaseStorage firebaseStorage;

    OnClickItem onClickItem;

    public void ItemOnClick(OnClickItem onClickItem){
        this.onClickItem = onClickItem;
    }

    Context context;

    public void SetData(List<News> listNews){
        this.listNews = listNews;
    }

    @NonNull
    @NotNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListNewsBinding listNewsBinding = ListNewsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        firebaseStorage = FirebaseStorage.getInstance();
        context = parent.getContext();
        return new NewViewHolder(listNewsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewViewHolder holder, int position) {
        News news = listNews.get(position);
        StorageReference reference = firebaseStorage.getReference().child(news.getThumbnailPath());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .error(R.drawable.image_notavailable)
                        .into(holder.listNewsBinding.thumbnail);
            }
        });
        holder.listNewsBinding.subTitle.setText(news.getSub_title());
        holder.listNewsBinding.itemNew.setOnClickListener(view -> {
            onClickItem.ItemOnclick(news);
        });
    }

    @Override
    public int getItemCount() {
        return listNews.isEmpty()?0: listNews.size();
    }

    public static class NewViewHolder extends RecyclerView.ViewHolder {
        ListNewsBinding listNewsBinding;
        public NewViewHolder(ListNewsBinding listNewsBinding) {
            super(listNewsBinding.getRoot());
            this.listNewsBinding = listNewsBinding;
        }
    }

    public interface OnClickItem{
        void ItemOnclick(News news);
    }
}
