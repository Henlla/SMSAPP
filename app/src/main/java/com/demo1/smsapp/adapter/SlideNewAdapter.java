package com.demo1.smsapp.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.SlideNewsBinding;
import com.demo1.smsapp.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SlideNewAdapter extends SliderViewAdapter<SlideNewAdapter.SlideNewVH> {

    List<News> list;

    private FirebaseStorage firebaseStorage;
    Context context;

    public void setData( List<News> list){
        this.list = list;
    }

    @Override
    public SlideNewVH onCreateViewHolder(ViewGroup parent) {
        SlideNewsBinding slideNewsBinding = SlideNewsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        firebaseStorage = FirebaseStorage.getInstance();
        context = parent.getContext();
        return new SlideNewVH(slideNewsBinding);
    }

    @Override
    public void onBindViewHolder(SlideNewVH viewHolder, int position) {
        News news = list.get(position);
        StorageReference reference = firebaseStorage.getReference().child(news.getThumbnailPath());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .error(R.drawable.image_notavailable)
                        .into(viewHolder.slideNewsBinding.thumbnail);
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public static class SlideNewVH extends SliderViewAdapter.ViewHolder {

        SlideNewsBinding slideNewsBinding;

        public SlideNewVH(SlideNewsBinding slideNewsBinding) {
            super(slideNewsBinding.getRoot());
            this.slideNewsBinding = slideNewsBinding;
        }
    }
}
