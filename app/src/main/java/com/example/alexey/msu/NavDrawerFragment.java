package com.example.alexey.msu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 04.12.2014.
 */

public class NavDrawerFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;//диалог загрузки
    private List<ArticleItem> articleList;//массив с типом Статья
    private ListView listView;
    private ArticleItemAdapter adapter;

    private static final String bundleCategory = "categoryId";

    public static final String KEY_TITLE="title";
    public static final String KEY_CONTENT="content";
    public static final String KEY_PHOTO="main_img_url";
    public static final String KEY_PHOTO_GALLERY="photo_attachments";
    public static final String KEY_POSITION = "";
    public static final String KEY_VIDEO= "video_attachments";

    private String[] navMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private String selectedCategory;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter navAdapter;

    public Cache cache = AppController.getInstance().getRequestQueue().getCache();
    private static final String URL_FEED = "http://msulife.com/api/getPosts";

    public NavDrawerFragment() {
    }

    public static NavDrawerFragment newInstance(String category) {
        NavDrawerFragment f = new NavDrawerFragment();
        Bundle args = new Bundle();
        args.putString(bundleCategory, category);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.article_item, container,
                false);
        //gridView = (GridView) rootView.findViewById(R.id.grid_view);
        listView = (ListView) rootView.findViewById(R.id.list);

        articleList = new ArrayList<ArticleItem>();

        // Getting Album Id of the item selected in navigation drawer
        // if Album Id is null, user is selected recently added option
        if (getArguments().getString(bundleCategory) != null) {
            selectedCategory = getArguments().getString(bundleCategory);
            Log.d(TAG,
                    "Selected album id: "
                            + getArguments().getString(bundleCategory));
        } else {
            Log.d(TAG, "Selected recently added album");
            selectedCategory = null;
        }

        Cache.Entry entry = cache.get(URL_FEED);

        // Showing progress dialog before making http request
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Загрузка...");
        pDialog.show();



        //кэш volley
        /*if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONArray(data), selectedCategory);
                    //не забудь про эту штуку
                    hidePDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {*/
            cache.clear();

            JsonArrayRequest articleReq = new JsonArrayRequest(URL_FEED,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Log.d(TAG, response.toString());
                            hidePDialog();
                            if (response != null)
                                parseJsonFeed(response,selectedCategory);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    /*Toast.makeText(getActivity().getApplicationContext(),
                            "Не могу получить данные с сервера.\n" +
                                    "Проверьте ваше Интернет соединение.",
                            Toast.LENGTH_SHORT).show();*/
                    hidePDialog();
                }
            });
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(articleReq);
        //}

        adapter = new ArticleItemAdapter(getActivity(), articleList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long offset) {
                ArticleItem item = (ArticleItem) adapter.getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), ArticleActivity.class);

                intent.putExtra(KEY_TITLE, item.getTitle());
                intent.putExtra(KEY_CONTENT, item.getContent());
                intent.putExtra(KEY_PHOTO, item.getMain_img_url());
                intent.putExtra(KEY_PHOTO_GALLERY, item.getPhoto_attachments());
                intent.putExtra(KEY_POSITION, position);
                intent.putExtra(KEY_VIDEO, item.getVideo());

                startActivity(intent);
            }
        });

        return rootView;
    }

    private void parseJsonFeed(JSONArray response, String category){
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
                    String categories = catObject.getString("category_name");
                    cat.add(categories);
                }
                article.setCategories(cat);

                if (category!=null)
                    for(String catExcept : cat) {
                        if (catExcept.equals(category))
                            articleList.add(article);
                    }
                else
                    articleList.add(article);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        adapter.notifyDataSetChanged();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}
