package com.example.alexey.msu;

/**
 * Created by Alexey on 03.10.2014.
 *
 * Активити, в котором открывается статья с текстом
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import static com.example.alexey.msu.MainActivity.KEY_CONTENT;
import static com.example.alexey.msu.MainActivity.KEY_PHOTO;
import static com.example.alexey.msu.MainActivity.KEY_TITLE;


public class ArticleActivity extends Activity{

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        String title="",
               content="",
               photo="";

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        Intent intent = getIntent();
        if (null != intent) {
            title = intent.getStringExtra(KEY_TITLE);
            content = intent.getStringExtra(KEY_CONTENT);
            photo = intent.getStringExtra(KEY_PHOTO);
        }

        FeedImageView mainImg = (FeedImageView) findViewById(R.id.mainImg);

        mainImg.setImageUrl(photo,imageLoader);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        TextView tvContent = (TextView) findViewById(R.id.tvContent);
        tvContent.setText(content);
        tvContent.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
