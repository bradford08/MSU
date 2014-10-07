package com.example.alexey.msu;

/**
 * Created by Alexey on 03.10.2014.
 *
 * Собственно класс статей
 */
public class ArticleItem {

    private String
            title,
            description,
            content,
            author,
            photo_by,
            createdAt,
            updatedAt,
            id;

    public ArticleItem() {
        // TODO Auto-generated constructor stub
    }

    public ArticleItem(String title, String description, String content, String author,
                  String photo_by, String createdAt, String updatedAt, String id) {
        super();
        this.title = title;
        this.description = description;
        this.content = content;
        this.author = author;
        this.photo_by = photo_by;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhoto_by() {
        return photo_by;
    }

    public void setPhoto_by(String photo_by) {
        this.photo_by = photo_by;
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

}

