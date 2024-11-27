package com.jhw.carrot_market_clone_backend.model.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity(name = "post_image_hash")
public class PostImage {

    @Id
    @Column(name = "image_name")
    private String name;

    @Column(name = "image_number")
    private Integer imageNumber;

    @Transient
    private String contentType;

    @Transient
    private String preSignedUrl;

    @Column(name = "image_url")
    private String url;

    @Column(name = "post_id")
    private Integer postId;

    public PostImage(String name, Integer imageNumber, String contentType, String preSignedUrl, String url, Integer postId) {
        this.name = name;
        this.imageNumber = imageNumber;
        this.contentType = contentType;
        this.preSignedUrl = preSignedUrl;
        this.url = url;
        this.postId = postId;
    }

    // for thumbnail
    public PostImage(int postId, String url) {
        this.postId = postId;
        this.url = url;
    }

    // post's images list for update
    public PostImage(int postId, String url, String name, Integer imageNumber) {
        this.postId = postId;
        this.url = url;
        this.name = name;
        this.imageNumber = imageNumber;
    }


    // for post's image list
    public PostImage(String url, String name, Integer imageNumber) {
        this.url = url;
        this.name = name;
        this.imageNumber = imageNumber;
    }

    public PostImage() {}

    public String getName() { return name; }

    public String getContentType() { return contentType; }

    public String getPreSignedUrl() { return preSignedUrl; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public Integer getPostId() { return postId; }

    public void setPostId(Integer postId) { this.postId = postId; }

    public Integer getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(Integer imageNumber) {
        this.imageNumber = imageNumber;
    }
}
