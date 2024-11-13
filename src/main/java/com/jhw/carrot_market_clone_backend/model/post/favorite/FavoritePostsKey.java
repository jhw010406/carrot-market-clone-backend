package com.jhw.carrot_market_clone_backend.model.post.favorite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class FavoritePostsKey implements Serializable {

    @Column(name = "user_uid")
    private int userUid;

    @Column(name = "post_id")
    private int postId;

    public FavoritePostsKey(
        int userUid,
        int postId
    ) {
        this.userUid = userUid;
        this.postId = postId;
    }

    public FavoritePostsKey() {}

    public int getUserUid() { return userUid; }

    public void setUserUid(int userUid) { this.userUid = userUid; }

    public int getPostId() { return postId; }

    public void setPostId(int postId) { this.postId = postId; }
}
