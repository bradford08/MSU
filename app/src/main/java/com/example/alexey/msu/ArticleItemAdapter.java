package com.example.alexey.msu;

/**
 * Created by Alexey on 03.10.2014.
 *
 * Это адаптер для listview, который выводит
 * в текст вью преобразованные json данные
 */
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

public class ArticleItemAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ArticleItem> articleItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ArticleItemAdapter(Activity activity, List<ArticleItem> articleItems) {
        this.activity = activity;
        this.articleItems = articleItems;
    }

    @Override
    public int getCount() {
        return articleItems.size();
    }

    @Override
    public Object getItem(int location) {
        return articleItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.article_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);
        TextView tvUpdatedAt = (TextView) convertView.findViewById(R.id.tvUpdatedAt);

        FeedImageView photo = (FeedImageView) convertView.findViewById(R.id.photo);

        ArticleItem item = articleItems.get(position);

        //вывод в текст вью
        tvTitle.setText(articleItems.get(position).getTitle());
        tvAuthor.setText("By " + articleItems.get(position).getAuthor());
        tvDescription.setText(articleItems.get(position).getDescription());
        tvContent.setText(articleItems.get(position).getContent());
        tvId.setText("ID: " + articleItems.get(position).getId());
        tvCreatedAt.setText(articleItems.get(position).getCreatedAt());
        tvUpdatedAt.setText(articleItems.get(position).getUpdatedAt());

        // вывод изображения
        if (item.getPhoto_by() != null) {
            photo.setImageUrl(item.getPhoto_by(), imageLoader);
            photo.setVisibility(View.VISIBLE);
            photo.setResponseObserver(new FeedImageView.ResponseObserver() {
            @Override
            public void onError() {
            }
            @Override
            public void onSuccess() {
            }
            });
        } else {
            photo.setVisibility(View.GONE);
        }

        return convertView;
    }

}
