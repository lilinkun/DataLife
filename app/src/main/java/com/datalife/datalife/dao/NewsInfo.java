package com.datalife.datalife.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by LG on 2018/2/3.
 */
@Entity
public class NewsInfo {
    @Id
    private String article_id;
    private String cat_id;
    private String article_title;
    private String article_type;
    private String link;
    private String is_open;
    private String open_type;
    private String add_time;
    private String thumbnail;
    private String brand_id;
    private String article_description;
    private String article_author;

    @Generated(hash = 722837998)
    public NewsInfo(String article_id, String cat_id, String article_title,
            String article_type, String link, String is_open, String open_type,
            String add_time, String thumbnail, String brand_id,
            String article_description, String article_author) {
        this.article_id = article_id;
        this.cat_id = cat_id;
        this.article_title = article_title;
        this.article_type = article_type;
        this.link = link;
        this.is_open = is_open;
        this.open_type = open_type;
        this.add_time = add_time;
        this.thumbnail = thumbnail;
        this.brand_id = brand_id;
        this.article_description = article_description;
        this.article_author = article_author;
    }

    @Generated(hash = 859431180)
    public NewsInfo() {
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_type() {
        return article_type;
    }

    public void setArticle_type(String article_type) {
        this.article_type = article_type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getOpen_type() {
        return open_type;
    }

    public void setOpen_type(String open_type) {
        this.open_type = open_type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getArticle_description() {
        return article_description;
    }

    public void setArticle_description(String article_description) {
        this.article_description = article_description;
    }

    public String getArticle_author() {
        return article_author;
    }

    public void setArticle_author(String article_author) {
        this.article_author = article_author;
    }
}
