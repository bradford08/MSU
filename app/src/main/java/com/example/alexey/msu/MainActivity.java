package com.example.alexey.msu;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class MainActivity extends Activity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String url = "http://kanzitdinov.com/posts/getPosts"/*"http://m.uploadedit.com/b041/1412367445620.txt"*/;
    private ProgressDialog pDialog;//диалог загрузки
    private List<ArticleItem> articleList = new ArrayList<ArticleItem>();//массив с типом Статья
    private ListView listView;
    private ArticleItemAdapter adapter;

    //переменные, которые будут экспортироваться во второе активити
    public static final String KEY_TITLE="title";
    public static final String KEY_CONTENT="content";
    public static final String KEY_AUTHOR="author";
    //public static final String KEY_PHOTO="photo_by";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new ArticleItemAdapter(this, articleList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        //обработчик клика по элементам лист вью
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long offset) {
                ArticleItem item = (ArticleItem) adapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                intent.putExtra(KEY_TITLE, item.getTitle());
                intent.putExtra(KEY_CONTENT, item.getContent());
                //intent.putExtra(KEY_PHOTO, item.getPhoto_by());
                intent.putExtra(KEY_AUTHOR, item.getAuthor());

                startActivity(intent);
            }
        });

        // запрос json объекта через библиотеку volley
        JsonArrayRequest articleReq = new JsonArrayRequest(url,
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
                                article.setAuthor(obj.getString("author"));
                                article.setDescription(obj.getString("description"));
                                article.setContent(obj.getString("content"));
                                article.setPhoto_by(obj.getString("photo_by"));
                                article.setId(obj.getString("id"));
                                article.setCreatedAt(obj.getString("createdAt"));
                                article.setUpdatedAt(obj.getString("updatedAt"));

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

                Toast.makeText(getApplicationContext(), "Unable to fetch data from server. Check your Internet connection.", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
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