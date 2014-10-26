package com.example.alexey.msu;

/**
 * Created by Alexey on 03.10.2014.
 *
 * Активити, в котором открывается статья с текстом
 */
import android.app.ActionBar;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
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

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                /*Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;*/
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
