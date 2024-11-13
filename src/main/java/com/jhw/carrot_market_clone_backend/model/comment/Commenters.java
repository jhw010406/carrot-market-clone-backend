package com.jhw.carrot_market_clone_backend.model.comment;

import jakarta.persistence.*;

@Entity(name = "commenters")
public class Commenters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;

    @Column(name = "commenter_uid")
    private int commenterUID;

    @Column(name = "target_post_id")
    private int targetPostID;

    public Commenters(
            Integer id,
            int commenterUID,
            int targetPostID
    ) {
        this.id = id;
        this.commenterUID = commenterUID;
        this.targetPostID = targetPostID;
    }

    public Commenters() {}

    public Integer getId() { return id; }

    public int getCommenterUID() { return commenterUID; }

    public int getTargetPostID() { return targetPostID; }
}
