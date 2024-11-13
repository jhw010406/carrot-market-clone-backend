package com.jhw.carrot_market_clone_backend.model.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "post_details")
public class PostDetails {

    @Id
    @Column(name = "post_id")
    private int postID;

    @Column(name = "user_uid")
    private int posterUID;

    @Transient
    private String posterID;

    @Transient
    private int category;

    @Transient
    private String thumbnailImageUrl;

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

    @Column(name = "post_view_count")
    private int viewCount;

    @CreatedDate
    @Column(name = "post_upload_date")
    private LocalDateTime uploadDate;

    @UpdateTimestamp
    @Column(name = "post_modify_date")
    private LocalDateTime modifiedDate;

    @Transient
    public PostDetailsTrading postDetailsTrading;

    @Transient
    public PostDetailsCommunity postDetailsCommunity;

    public PostDetails(
        int postID,
        int posterUID,
        String posterID,
        int category,
        String thumbnailImageUrl,
        String title,
        String content,
        int viewCount,
        LocalDateTime uploadDate,
        LocalDateTime modifiedDate,
        PostDetailsTrading postDetailsTrading,
        PostDetailsCommunity postDetailsCommunity
    ){
        this.postID = postID;
        this.posterUID = posterUID;
        this.posterID = posterID;
        this.category = category;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.uploadDate = uploadDate;
        this.modifiedDate = modifiedDate;
        this.postDetailsTrading = postDetailsTrading;
        this.postDetailsCommunity = postDetailsCommunity;
    }

    public PostDetails(
            int postID,
            int posterUID,
            String title,
            LocalDateTime uploadDate
    ) {
        this.postID = postID;
        this.posterUID = posterUID;
        this.title = title;
        this.uploadDate = uploadDate;
    }

    public PostDetails() {}

    public int getPostID() { return postID; }

    public void setPostID(int postID) { this.postID = postID; }

    public String getPosterID() { return posterID; }

    public void setPosterID(String posterID) { this.posterID = posterID; }

    public int getPosterUID() { return posterUID; }

    public int getCategory() { return category; }

    public String getThumbnailImageUrl() { return thumbnailImageUrl; }

    public void setThumbnailImageUrl(String thumbnailImageUrl) { this.thumbnailImageUrl = thumbnailImageUrl; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }

    public int getViewCount() { return viewCount; }

    public LocalDateTime getUploadDate() { return uploadDate; }

    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }

    public LocalDateTime getModifiedDate() { return modifiedDate; }

    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }

    public void setPostDetailsTrading(PostDetailsTrading postDetailsTrading) { this.postDetailsTrading = postDetailsTrading; }

    public void setPostDetailsCommunity(PostDetailsCommunity postDetailsCommunity) { this.postDetailsCommunity = postDetailsCommunity; }
}
