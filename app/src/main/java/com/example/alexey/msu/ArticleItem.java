package com.example.alexey.msu;

import java.util.ArrayList;

/**
 * Created by Alexey on 03.10.2014.
 *
 * Собственно класс статей
 */
public class ArticleItem {

    private String
            content,
            post_createdAt,
            main_img_url,
            title,
            title_for_url,
            post_updatedAt,
            post_id,
            category_name,
            category_name_for_url,
            createdAt,
            updatedAt,
            id,
            video;

    private ArrayList<String> categories;
    private ArrayList<String> photo_attachments;
    private ArrayList<String> video_attachments;

    public ArticleItem() {
    }

    public ArticleItem(ArrayList<String> categories,
                       String content,
                       String post_createdAt,
                       String main_img_url,
                       ArrayList<String> photo_attachments,
                       String title,
                       String post_id,
                       String post_updatedAt,
                       ArrayList<String> video_attachments,
                       String video/*,
                       String category_name*/) {
        super();
        this.categories=categories;
        this.setContent(content);
        this.setPost_createdAt(post_createdAt);
        this.setMain_img_url(main_img_url);
        this.photo_attachments = photo_attachments;
        this.setTitle(title);
        this.setPost_updatedAt(post_updatedAt);
        this.video_attachments = video_attachments;
        this.setPost_id(post_id);
        this.setVideo(video);
        //this.setCategory_name(category_name);
    }
    /*public ArticleItem(ArrayList<String> categories,
                       String content,
                       String post_createdAt,
                       String main_img_url,
                       ArrayList<String> photo_attachments,
                       String title,
                       String title_for_url,
                       String post_updatedAt,
                       ArrayList<String> video_attachments,
                       String post_id,
                       String category_name,
                       String category_name_for_url,
                       String createdAt,
                       String updatedAt,
                       String id) {
        super();
        this.setCategories(categories);
        this.setContent(content);
        this.setPost_createdAt(post_createdAt);
        this.setMain_img_url(main_img_url);
        this.photo_attachments = photo_attachments;
        this.setTitle(title);
        this.setTitle_for_url(title_for_url);
        this.setPost_updatedAt(post_updatedAt);
        this.video_attachments = video_attachments;
        this.setPost_id(post_id);
        this.setCategory_name(category_name);
        this.setCategory_name_for_url(category_name_for_url);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
        this.setId(id);
    }*/

    public ArrayList<String> getPhoto_attachments() {
        return photo_attachments;
    }

    public void setPhoto_attachments(ArrayList<String> photo_attachments) {
        this.photo_attachments = photo_attachments;
    }

    public ArrayList<String> getVideo_attachments() {
        return video_attachments;
    }

    public void setVideo_attachments(ArrayList<String> video_attachments) {
        this.video_attachments = video_attachments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPost_createdAt() {
        return post_createdAt;
    }

    public void setPost_createdAt(String post_createdAt) {
        this.post_createdAt = post_createdAt;
    }

    public String getMain_img_url() {
        return main_img_url;
    }

    public void setMain_img_url(String main_img_url) {
        this.main_img_url = main_img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_for_url() {
        return title_for_url;
    }

    public void setTitle_for_url(String title_for_url) {
        this.title_for_url = title_for_url;
    }

    public String getPost_updatedAt() {
        return post_updatedAt;
    }

    public void setPost_updatedAt(String post_updatedAt) {
        this.post_updatedAt = post_updatedAt;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name_for_url() {
        return category_name_for_url;
    }

    public void setCategory_name_for_url(String category_name_for_url) {
        this.category_name_for_url = category_name_for_url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}

