package com.jhw.carrot_market_clone_backend.model.post.favorite;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity(name = "favorite_posts")
public class FavoritePosts {

    @EmbeddedId
    private FavoritePostsKey id;

    public FavoritePosts(
            FavoritePostsKey inputId
    ) {
        this.id = inputId;
    }

    public FavoritePosts() {}

    public int getUserUid() { return this.id.getUserUid(); }

    public int getPostId() { return this.id.getPostId(); }
}
