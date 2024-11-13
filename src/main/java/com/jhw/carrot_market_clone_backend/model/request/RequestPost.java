package com.jhw.carrot_market_clone_backend.model.request;

public class RequestPost {
    private final Integer userUID;

    private final int postCategory;

    private final int startNumber;

    private final int postsCount;

    public RequestPost(Integer userUID, int postCategory, int startNumber, int postsCount) {
        this.userUID = userUID;
        this.postCategory = postCategory;
        this.startNumber = startNumber;
        this.postsCount = postsCount;
    }

    public Integer getUserUID() { return userUID; }

    public int getPostCategory() { return postCategory; }

    public int getStartNumber() { return startNumber; }

    public int getPostsCount() { return postsCount; }
}
