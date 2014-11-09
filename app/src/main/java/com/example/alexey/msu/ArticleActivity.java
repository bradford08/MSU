package com.example.alexey.msu;

/**
 * Created by Alexey on 03.10.2014.
 *
 * Активити, в котором открывается статья с текстом
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import static com.example.alexey.msu.MainActivity.KEY_CONTENT;
import static com.example.alexey.msu.MainActivity.KEY_PHOTO;
import static com.example.alexey.msu.MainActivity.KEY_TITLE;
import static com.example.alexey.msu.MainActivity.KEY_PHOTO_GALLERY;
import static com.example.alexey.msu.MainActivity.KEY_POSITION;
import static com.example.alexey.msu.MainActivity.KEY_VIDEO;

public class ArticleActivity extends Activity{

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private static final String TAG = "MyLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);
        Button btnImg = (Button) findViewById(R.id.btnImg);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String title = "",
               content = "",
               photo = "",
               video = "";

        ArrayList<String> photoGallery = new ArrayList<String>();

        int position = 0;

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        Intent intent = getIntent();
        if (null != intent) {
            title = intent.getStringExtra(KEY_TITLE);
            content = intent.getStringExtra(KEY_CONTENT);
            photo = intent.getStringExtra(KEY_PHOTO);
            position = intent.getIntExtra(KEY_POSITION, 0);
            photoGallery = intent.getStringArrayListExtra(KEY_PHOTO_GALLERY);
            video = intent.getStringExtra(KEY_VIDEO);
        }

        Log.d(TAG, video);//log

        FeedImageView mainImg = (FeedImageView) findViewById(R.id.mainImg);

        mainImg.setImageUrl(photo, imageLoader);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        TextView tvContent = (TextView) findViewById(R.id.tvContent);
        tvContent.setText(content);

        TextView tvVideo= (TextView) findViewById(R.id.tvVideo);
        if (video != null) {
            String vid = "<a href=\"" + video + "\">"
                               + video + "</a> ";
            tvVideo.setText(Html.fromHtml(vid));
            // Making url clickable
            tvVideo.setMovementMethod(LinkMovementMethod.getInstance());
            tvVideo.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            tvVideo.setVisibility(View.GONE);
        }

        final int finalPosition = position;
        final ArrayList<String> photoGalleryFinal = photoGallery;
        btnImg.setText("Всего фотографий: " + photoGalleryFinal.size());
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FullScreenImageActivity.class);

                intent.putExtra(KEY_PHOTO_GALLERY, photoGalleryFinal);
                intent.putExtra(KEY_POSITION, finalPosition);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_refresh).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                //finish();
                return true;
            case R.id.action_settings:
                Intent intentAbt = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intentAbt);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
