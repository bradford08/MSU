package com.example.alexey.msu;

/**
 * Created by Alexey on 24.11.2014.
 */
/*public class NavDrawerItem {

    private String albumId, albumTitle;
    // boolean flag to check for recent album
    private boolean isRecentAlbum = false;

    public NavDrawerItem() {
    }

    public NavDrawerItem(String albumId, String albumTitle) {
        this.albumId = albumId;
        this.albumTitle = albumTitle;
    }

    public NavDrawerItem(String albumId, String albumTitle,
                         boolean isRecentAlbum) {
        this.albumTitle = albumTitle;
        this.isRecentAlbum = isRecentAlbum;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return this.albumTitle;
    }

    public void setTitle(String title) {
        this.albumTitle = title;
    }

    public boolean isRecentAlbum() {
        return isRecentAlbum;
    }

    public void setRecentAlbum(boolean isRecentAlbum) {
        this.isRecentAlbum = isRecentAlbum;
    }
}*/
public class NavDrawerItem {

    private String title;
    private int icon;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem(){}

    public NavDrawerItem(String title){
        this.title = title;
    }

    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}