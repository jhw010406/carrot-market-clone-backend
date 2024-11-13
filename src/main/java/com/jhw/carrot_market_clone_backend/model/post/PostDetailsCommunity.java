package com.jhw.carrot_market_clone_backend.model.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "post_details_community")
public class PostDetailsCommunity {

    @Id
    @Column(name = "post_id")
    private int id;

    @Column(name = "post_subject")
    private String subject;

    public PostDetailsCommunity(
            int id,
            String subject
    ) {
        this.id = id;
        this.subject = subject;
    }

    public PostDetailsCommunity() {}

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public String getSubject() { return subject; }
}
