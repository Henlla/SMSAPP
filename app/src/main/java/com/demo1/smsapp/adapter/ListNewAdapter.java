package com.demo1.smsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ListListNewsBinding;
import com.demo1.smsapp.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListNewAdapter extends RecyclerView.Adapter<ListNewAdapter.ListNewViewHolder> {

    public List<News> listNews;
    private FirebaseStorage firebaseStorage;


    public void setData(List<News> listNews){
        this.listNews = listNews;
    }
    OnClickItem onClickItem;

    public void ItemOnClick(OnClickItem onClickItem){
        this.onClickItem = onClickItem;
    }
    Context context;
    @NonNull
    @NotNull
    @Override
    public ListNewViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListListNewsBinding listNewsBinding = ListListNewsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        firebaseStorage = FirebaseStorage.getInstance();
        context = parent.getContext();
        return new ListNewViewHolder(listNewsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListNewViewHolder holder, int position) {
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
        holder.listNewsBinding.postDate.setText(news.getPostdate());
        holder.listNewsBinding.itemNew.setOnClickListener(view -> {
            onClickItem.ItemOnclick(news);
        });
    }

    @Override
    public int getItemCount() {
        return listNews.isEmpty() ? 0: listNews.size();
    }

    public static class ListNewViewHolder extends RecyclerView.ViewHolder{
        ListListNewsBinding listNewsBinding;
        public ListNewViewHolder(ListListNewsBinding listNewsBinding) {
            super(listNewsBinding.getRoot());
            this.listNewsBinding = listNewsBinding;
        }
    }

    public interface OnClickItem{
        void ItemOnclick(News news);
    }
}
