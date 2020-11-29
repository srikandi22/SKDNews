package com.kita.skdnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kita.skdnews.MainActivity;
import com.kita.skdnews.R;
import com.kita.skdnews.db.DSNews;

import java.text.SimpleDateFormat;
import java.util.List;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ArchiveViewHolder> {
    private static String TAG = ArchiveAdapter.class.getSimpleName();
    private List<DSNews> newsList;
    private Context context;
    private ArchiveAdapter.OnItemClickListener onItemClickListener;

    public ArchiveAdapter(Context context, List<DSNews> newsList) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ArchiveAdapter.ArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.arch_item, parent, false);
        return new ArchiveAdapter.ArchiveViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchiveAdapter.ArchiveViewHolder holder, int position) {
        DSNews news = newsList.get(position);

        holder.title.setText(news.title);
        holder.desc.setText(news.desc);
        holder.src.setText(news.src_name);
        holder.author.setText(news.author);
        SimpleDateFormat dateFormat = new SimpleDateFormat(MainActivity.TIME_STAMP_FORMAT);
        holder.pub_date.setText(dateFormat.format(news.getDate()));

        Glide.with(context)
                .load(news.url_to_img)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void setOnItemClickListener(ArchiveAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ArchiveViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        TextView title, desc, src, author, pub_date;
        ImageView img;
        ArchiveAdapter.OnItemClickListener onItemClickListener;

        public ArchiveViewHolder(View itemView, ArchiveAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);

            title = itemView.findViewById(R.id.arch_item_title);
            desc = itemView.findViewById(R.id.arch_item_desc);
            src = itemView.findViewById(R.id.arch_item_src);
            author = itemView.findViewById(R.id.arch_item_author);
            pub_date = itemView.findViewById(R.id.arch_item_date);
            img = itemView.findViewById(R.id.arch_item_img);

            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
