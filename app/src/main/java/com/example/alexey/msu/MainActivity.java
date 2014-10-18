package com.example.alexey.msu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new ArticleItemAdapter(this, articleList);
        listView.setAdapter(adapter);
        //listView.setCacheColorHint(0);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        //обработчик клика по элементам
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long offset) {
                ArticleItem item = (ArticleItem) adapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                intent.putExtra(KEY_TITLE, item.getTitle());
                intent.putExtra(KEY_CONTENT, item.getContent());
                intent.putExtra(KEY_PHOTO, item.getMain_img_url());

                startActivity(intent);
            }
        });

        JsonArrayRequest articleReq = new JsonArrayRequest(URL_FEED,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

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

                                // video is json array
                                JSONArray videoArray = obj.getJSONArray("video_attachments");
                                ArrayList<String> video = new ArrayList<String>();
                                for (int j = 0; j < videoArray.length(); j++) {
                                    video.add((String) videoArray.get(j));
                                }
                                article.setVideo_attachments(video);

                                JSONArray catArray = obj.getJSONArray("categories");
                                ArrayList<String> cat = new ArrayList<String>();
                                for (int j = 0; j < catArray.length(); j++) {
                                    JSONObject catObj = (JSONObject) catArray.get(j);
                                    cat.add((String)catObj.getString("category_name"));
                                }
                                article.setCategories(cat);

                                // adding movie to movies array
                                articleList.add(article);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hidePDialog();
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(articleReq);
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
}