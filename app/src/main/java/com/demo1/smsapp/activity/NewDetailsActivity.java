package com.demo1.smsapp.activity;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ActivityNewDetailsBinding;
import com.demo1.smsapp.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class NewDetailsActivity extends AppCompatActivity {
    ActivityNewDetailsBinding newDetailsBinding;
    Gson gson;
    News news;

    private FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newDetailsBinding = ActivityNewDetailsBinding.inflate(getLayoutInflater());
        setContentView(newDetailsBinding.getRoot());
        gson = new Gson();
        firebaseStorage = FirebaseStorage.getInstance();
        String getData = getIntent().getStringExtra("newJson");
        news = gson.fromJson(getData,News.class);
        setUpNew();
        OnBack();
    }

    private void OnBack() {
        newDetailsBinding.icBack.setOnClickListener(view -> {
            finish();
        });
    }

    public void setUpNew(){
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        newDetailsBinding.subTitle.setText(news.getSub_title());
        newDetailsBinding.title.setText(news.getTitle());
        newDetailsBinding.postDate.setText(news.getPostdate());
        StorageReference reference = firebaseStorage.getReference().child(news.getThumbnailPath());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .error(R.drawable.image_notavailable)
                        .into(newDetailsBinding.thumbnail);
            }
        });

        newDetailsBinding.content.loadDataWithBaseURL("",news.getContent(),mimeType,encoding,"");


    }

}