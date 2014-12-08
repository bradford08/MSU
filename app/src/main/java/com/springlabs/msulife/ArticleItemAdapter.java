package com.springlabs.msulife;

/**
 * Created by Alexey on 03.10.2014.
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
import com.example.alexey.msulife.R;

public class ArticleItemAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ArticleItem> articleItems;
    private Context context;

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
            convertView = inflater.inflate(R.layout.article_item,null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView tvCategories = (TextView) convertView.findViewById(R.id.tvCategories);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        TextView tvCount = (TextView) convertView.findViewById(R.id.tvCount);

        FeedImageView mainImg = (FeedImageView) convertView.findViewById(R.id.mainImg);

        ArticleItem item = articleItems.get(position);

        tvTitle.setText(articleItems.get(position).getTitle());
        tvContent.setText(articleItems.get(position).getContent());
        tvCount.setText(articleItems.get(position).getWatchcount());

        // category
        String catStr = "Категории: ";
        for (String cat : articleItems.get(position).getCategories()) {
            catStr += cat + " ";
        }
        tvCategories.setText(catStr);

        // Checking for null feed url
        /*if (item.getVideo() != null) {
            String vid = "<a href=\"" + item.getVideo() + "\">"
                               + item.getVideo() + "</a> ";
            tvVideoAttach.setText(Html.fromHtml(vid));
            // Making url clickable
            tvVideoAttach.setMovementMethod(LinkMovementMethod.getInstance());
            tvVideoAttach.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            tvVideoAttach.setVisibility(View.GONE);
        }*/

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
