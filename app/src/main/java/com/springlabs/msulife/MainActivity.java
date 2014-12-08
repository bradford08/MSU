package com.springlabs.msulife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alexey.msulife.R;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String URL_FEED = "http://msulife.com/api/getPosts";
    private ProgressDialog pDialog;//диалог загрузки

    public static final String KEY_TITLE="title";
    public static final String KEY_CONTENT="content";
    public static final String KEY_PHOTO="main_img_url";
    public static final String KEY_PHOTO_GALLERY="photo_attachments";
    public static final String KEY_POSITION = "";
    public static final String KEY_VIDEO= "video_attachments";

    private String[] navMenuTitles;
    private DrawerLayout mDrawerLayout;
    //private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavDrawerItem> navDrawerItems;

    //EXPANDBURR
    ExpandableListAdapter navAdapter;
    ExpandableListView mDrawerList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.list_slidermenu);

        prepareListData();

        navAdapter = new NavDrawerExpandableListAdapter(this,
                listDataHeader, listDataChild);
        mDrawerList.setAdapter(navAdapter);

        // Listview Group click listener
        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (listDataChild.get(listDataHeader.get(groupPosition)) == null) {
                    mDrawerList.setGroupIndicator(null);
                    mTitle = listDataHeader.get(groupPosition);
                    displayView((String) mTitle);
                    return false;
                } else {
                    return false;
                }
            }
        });
        // Listview on child click listener
        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)
                        .equals("Вся рубрика")) {
                    mTitle = listDataHeader.get(groupPosition);
                    displayView((String) mTitle);

                } else {
                    mTitle = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                    displayView((String) mTitle);
                }
                return false;
            }
        });

        // Enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView("Все новости");
        }

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Все новости");

        listDataHeader.add("Новости");
        listDataHeader.add("Университет");
        listDataHeader.add("Лица");
        listDataHeader.add("Медиа");
        listDataHeader.add("Мир");
        listDataHeader.add("Развлечения");
        listDataHeader.add("Разное");


        // Adding child data
        List<String> news = new ArrayList<String>();
        news.add("Вся рубрика");
        news.add("Хроника");
        news.add("Детали");

        List<String> university = new ArrayList<String>();
        university.add("Вся рубрика");
        university.add("Учеба");
        university.add("Спорт");
        university.add("Наука");
        university.add("Проекты");
        university.add("Жизнь");
        university.add("Барахолка");

        List<String> faces = new ArrayList<String>();
        faces.add("Вся рубрика");
        faces.add("Дело");
        faces.add("Творчество");
        faces.add("Мнения");

        List<String> media = new ArrayList<String>();
        media.add("Вся рубрика");
        media.add("Фото");
        media.add("Видео");

        List<String> entertainment = new ArrayList<String>();
        entertainment.add("Вся рубрика");
        entertainment.add("Места");
        entertainment.add("События");
        entertainment.add("Концерты");
        entertainment.add("Театр");
        entertainment.add("Киноклубы");

        listDataChild.put(listDataHeader.get(0), null);
        listDataChild.put(listDataHeader.get(1), news); // Header, Child data
        listDataChild.put(listDataHeader.get(2), university);
        listDataChild.put(listDataHeader.get(3), faces);
        listDataChild.put(listDataHeader.get(4), media);
        listDataChild.put(listDataHeader.get(5), null);
        listDataChild.put(listDataHeader.get(6), entertainment);
        listDataChild.put(listDataHeader.get(7), null);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(String position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        if (position.equals("Все новости")) {
            fragment = NavDrawerFragment.newInstance(null);
        } else {
            fragment = NavDrawerFragment.newInstance(position);
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
            setTitle(position);
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
        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
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
                displayView("Все новости");
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