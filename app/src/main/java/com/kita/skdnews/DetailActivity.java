package com.kita.skdnews;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_detail);

        TextView title = findViewById(R.id.title);
        TextView content = findViewById(R.id.content);
        TextView url = findViewById(R.id.selengkapnya);
        ImageView image = findViewById(R.id.image_new);

        title.setText(getIntent().getStringExtra("title"));
        content.setText(getIntent().getStringExtra( "content"));
        url.setText("Selengkapnya: "+ getIntent().getStringExtra("url"));
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("getUrlToImage"))
                .into(image);
    }
}