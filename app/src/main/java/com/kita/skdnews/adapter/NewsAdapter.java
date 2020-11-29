package com.kita.skdnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kita.skdnews.DetailActivity;
import com.kita.skdnews.R;
import com.kita.skdnews.helper.Extra;
import com.kita.skdnews.models.Article;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private static String TAG = NewsAdapter.class.getSimpleName();
    private ArrayList<Article> articles;
    private Context context;

    public NewsAdapter(ArrayList<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        final Article model = articles.get(position);

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.source.setText(model.getSource().getName());
        holder.time.setText(" \u2022 " + Extra.DateToTimeFormat(context, model.getPublishedAt()));
        holder.published_ad.setText(Extra.DateFormat(model.getPublishedAt()));
        holder.author.setText(model.getAuthor());

        Glide.with(context)
                .load(model.getUrlToImage())
                .into(holder.imageView);
        holder.news_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("title", model.getTitle());
                intent.putExtra("content", model.getContent());
                intent.putExtra("url", model.getUrl());
                intent.putExtra("getUrlToImage", model.getUrlToImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        TextView title, desc, author, published_ad, source, time;
        ImageView imageView;
        CardView news_item;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.news_item_title);
            desc = itemView.findViewById(R.id.news_item_desc);
            author = itemView.findViewById(R.id.news_item_author);
            published_ad = itemView.findViewById(R.id.news_item_date);
            source = itemView.findViewById(R.id.news_item_src);
            time = itemView.findViewById(R.id.news_item_duration);
            imageView = itemView.findViewById(R.id.news_item_img);
            news_item = itemView.findViewById(R.id.news_item);
        }
    }
}
