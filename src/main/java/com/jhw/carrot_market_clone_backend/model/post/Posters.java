package com.jhw.carrot_market_clone_backend.model.post;

import jakarta.persistence.*;

@Entity(name = "posters")
public class Posters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postID;

    @Column(name = "user_uid")
    private int posterUID;

    @Column(name = "post_category")
    private int category;

    public Posters(
            Integer postID,
            int posterUID,
            int category
    ){
        this.postID = postID;
        this.posterUID = posterUID;
        this.category = category;
    }

    public Posters() {}

    public int getPostID() { return this.postID; }

    public int getUserUID() { return this.posterUID; }

    public int getCategory() { return this.category; }
}
