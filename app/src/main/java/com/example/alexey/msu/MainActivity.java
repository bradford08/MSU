package com.example.alexey.msu;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String URL_FEED = "http://kanzitdinov.com/api/getPosts";
    private ProgressDialog pDialog;//диалог загрузки
    private List<ArticleItem> articleList = new ArrayList<ArticleItem>();//массив с типом Статья
    private ListView listView;
    private ArticleItemAdapter adapter;

    //переменные, которые будут экспортироваться во второе активити
    public static final String KEY_TITLE="title";
    public static final String KEY_CONTENT="content";
    public static final String KEY_PHOTO="main_img_url";
    public static final String KEY_PHOTO_GALLERY="photo_attachments";
    public static final String KEY_POSITION = "";
    public static final String KEY_VIDEO= "video_attachments";

    private MenuItem refreshMenuItem;

    public Cache cache = AppController.getInstance().getRequestQueue().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);

        volleyWork();

        //обработчик клика по элементам
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long offset) {
                ArticleItem item = (ArticleItem) adapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                intent.putExtra(KEY_TITLE, item.getTitle());
                intent.putExtra(KEY_CONTENT, item.getContent());
                intent.putExtra(KEY_PHOTO, item.getMain_img_url());
                intent.putExtra(KEY_PHOTO_GALLERY, item.getPhoto_attachments());
                intent.putExtra(KEY_POSITION, position);
                intent.putExtra(KEY_VIDEO, item.getVideo());

                startActivity(intent);
            }
        });

    }

    private void volleyWork() {
        cache.clear();//не забудь
        Entry entry = cache.get(URL_FEED);

        // Showing progress dialog before making http request
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Загрузка...");
        pDialog.show();

        adapter = new ArticleItemAdapter(this, articleList);
        listView.setAdapter(adapter);

        //кэш volley
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONArray(data));
                    //не забудь про эту штуку
                    hidePDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            cache.clear();

            JsonArrayRequest articleReq = new JsonArrayRequest(URL_FEED,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            hidePDialog();
                            if (response != null)
                                parseJsonFeed(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Не могу получить данные с сервера.\n" +
                                    "Проверьте ваше Интернет соединение.",
                            Toast.LENGTH_SHORT).show();
                    hidePDialog();
                }
            });
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(articleReq);
        }
    }

    private void parseJsonFeed(JSONArray response){
        // Parsing json
        for (int i = 0; i < response.length(); i++) {
            try {

                JSONObject obj = response.getJSONObject(i);

                ArticleItem article = new ArticleItem();

                article.setTitle(obj.getString("title"));
                article.setContent(obj.getString("content"));
                article.setPost_createdAt(obj.getString("createdAt"));
                article.setPost_updatedAt(obj.getString("updatedAt"));
                article.setPost_id(obj.getString("id"));

                String image = obj.isNull("main_img_url") ? null : obj
                        .getString("main_img_url");
                article.setMain_img_url(image);

                JSONArray videoArray = obj.getJSONArray("video_attachments");
                //if (videoArray != null)
                  //  article.setVideo((String) videoArray.get(0));
                //else
                  //  article.setVideo(null);
                String video = videoArray.isNull(0) ? null : videoArray.getString(0);
                article.setVideo(video);

                JSONArray imgArray = obj.getJSONArray("photo_attachments");
                ArrayList<String> img = new ArrayList<String>();
                for (int j = 0; j < imgArray.length(); j++) {
                    img.add((String) imgArray.get(j));
                }
                article.setPhoto_attachments(img);

                JSONArray catArray = obj.getJSONArray("categories");
                ArrayList<String> cat = new ArrayList<String>();
                for (int j = 0; j < catArray.length(); j++) {
                    JSONObject catObject = catArray.getJSONObject(j);
                    String category = catObject.getString("category_name");
                    cat.add(category);
                }
                article.setCategories(cat);

                articleList.add(article);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshMenuItem = item;
                cache.clear();
                listView.setAdapter(null);
                volleyWork();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}