package com.example.alexey.msu;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
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

    private static final String URL_FEED = "http://msulife.com/api/getPosts";
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

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter navAdapter;

    public Cache cache = AppController.getInstance().getRequestQueue().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);

        volleyWork();

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        //navDrawerItems = new ArrayList<ArticleItem>();
/*
        for (ArticleItem a : navDrawerItems) {
            navDrawerItems.add(new ArticleItem(a.getTitle()));
        }*/

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // Setting the nav drawer list adapter

        navDrawerItems = new ArrayList<NavDrawerItem>();

        navDrawerItems.add(new NavDrawerItem("Хроника"));
        navDrawerItems.add(new NavDrawerItem("Детали"));
       /* navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem("");*/


        // setting the nav drawer list adapter
        navAdapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(navAdapter);


        // Enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

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

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    /**
     * Navigation drawer menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        /*switch (position) {
            case 0:
                // Recently added item selected
                // don't pass album id to home fragment
                fragment = GridFragment.newInstance(null);
                break;

            default:
                // selected wallpaper category
                // send album id to home fragment to list all the wallpapers
                String albumId = albumsList.get(position).getId();
                fragment = GridFragment.newInstance(albumId);
                break;
        }*/

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.list, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navDrawerItems.get(position).getTitle());
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
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
                //ArticleItem navArticle = new ArticleItem();

               // navArticle.setTitle(obj.getString("title"));
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

                //for(String catExcept : article.getId())
                    //if( article.getId()=="543268d9dcd9896d0c9494dc")
                        articleList.add(article);
                //navDrawerItems.add(navArticle);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        adapter.notifyDataSetChanged();
        //navAdapter.notifyDataSetChanged();
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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