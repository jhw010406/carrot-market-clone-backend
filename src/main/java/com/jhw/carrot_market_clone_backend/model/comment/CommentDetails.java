package com.jhw.carrot_market_clone_backend.model.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "comment_details")
public class CommentDetails {

    @Id
    @Column(name = "comment_id")
    private int id;

    @Column(name = "comment_content")
    private String content;

    @CreationTimestamp
    @Column(name = "comment_upload_date")
    private LocalDateTime uploadDate;

    @UpdateTimestamp
    @Column(name = "comment_modify_date")
    private LocalDateTime modifyDate;

    public CommentDetails(
            int id,
            String content,
            String targetPostCategory,
            LocalDateTime uploadDate,
            LocalDateTime modifyDate
    ) {
        this.id = id;
        this.content = content;
        this.uploadDate = uploadDate;
        this.modifyDate = modifyDate;
    }

    public CommentDetails() {}

    public int getId() { return id; }
}
