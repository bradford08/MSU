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
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

        TextView tvCategoryName = (TextView) convertView.findViewById(R.id.tvCategoryName);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        TextView tvVideoAttach = (TextView) convertView.findViewById(R.id.tvVideoAttach);
        TextView tvPostCreatedAt = (TextView) convertView.findViewById(R.id.tvPostCreatedAt);
        TextView tvPostUpdatedAt = (TextView) convertView.findViewById(R.id.tvPostUpdatedAt);
        TextView tvPostId = (TextView) convertView.findViewById(R.id.tvPostId);

        FeedImageView mainImg = (FeedImageView) convertView.findViewById(R.id.mainImg);

        ArticleItem item = articleItems.get(position);

        tvTitle.setText(articleItems.get(position).getTitle());
        tvContent.setText(articleItems.get(position).getContent());
        tvPostId.setText("ID: " + articleItems.get(position).getPost_id());
        tvPostCreatedAt.setText(articleItems.get(position).getPost_createdAt());
        tvPostUpdatedAt.setText(articleItems.get(position).getPost_createdAt());

        // category
        String catStr = "";
        for (String cat : articleItems.get(position).getCategories()) {
            cat=item.getCategory_name();
            catStr += cat + ", ";
        }
        tvCategoryName.setText(catStr);

        // Checking for null feed url
        if (item.getVideo_attachments() != null) {
            String vidStr="";
            for (String vid : articleItems.get(position).getVideo_attachments()) {
                vid = "<a href=\"" + item.getVideo_attachments() + "\">"
                                + item.getVideo_attachments() + "</a> ";
                vidStr += vid + "\n";
            }
            tvVideoAttach.setText(Html.fromHtml(vidStr));
            // Making url clickable
            //tvVideoAttach.setMovementMethod(LinkMovementMethod.getInstance());
            tvVideoAttach.setVisibility(View.VISIBLE);
        } else {
        // url is null, remove from the view
        tvVideoAttach.setVisibility(View.GONE);
        }

        // вывод изображения
        if (item.getMain_img_url() != null) {
            mainImg.setImageUrl(item.getMain_img_url(), imageLoader);
            mainImg.setVisibility(View.VISIBLE);
            mainImg.setResponseObserver(new FeedImageView.ResponseObserver() {
            @Override
            public void onError() {
            }
            @Override
            public void onSuccess() {
            }
            });
        } else {
            mainImg.setVisibility(View.GONE);
        }

        return convertView;
    }

}
