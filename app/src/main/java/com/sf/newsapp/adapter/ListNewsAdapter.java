package com.sf.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.sf.newsapp.NewsDetailActivity;
import com.sf.newsapp.R;
import com.sf.newsapp.helper.ISO8601DateParser;
import com.sf.newsapp.helper.ItemClickListener;
import com.sf.newsapp.model.Article;
import com.sf.newsapp.model.News;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mesutgenc on 31.01.2018.
 */


class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemClickListener itemClickListener;
    TextView article_title;
    CircleImageView article_image;
    RelativeTimeTextView article_time;

    public ListNewsViewHolder(View itemView) {
        super(itemView);
        article_image = (CircleImageView) itemView.findViewById(R.id.article_image);
        article_title = (TextView) itemView.findViewById(R.id.article_title);
        article_time = (RelativeTimeTextView) itemView.findViewById(R.id.article_time);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder> {
    private Context context;
    private List<Article> articleList;

    public ListNewsAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public ListNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.news_layout, parent, false);
        return new ListNewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListNewsViewHolder holder, int position) {

        Picasso.with(context)
                .load(articleList.get(position).getUrlToImage())
                .into(holder.article_image);

        if (articleList.get(position).getTitle().length() > 65) {
            holder.article_title.setText(articleList.get(position).getTitle().substring(0, 65) + "...");
        } else {
            holder.article_title.setText(articleList.get(position).getTitle());
        }

        Date date = null;
        try {
            date = ISO8601DateParser.parse(articleList.get(position).getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.article_time.setReferenceTime(date.getTime());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("webUrl", articleList.get(position).getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
