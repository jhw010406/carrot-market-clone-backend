package com.jhw.carrot_market_clone_backend.repository;

import com.jhw.carrot_market_clone_backend.model.post.PostDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostDetailsRepository extends JpaRepository<PostDetails, Integer> {

    @Query("select new com.jhw.carrot_market_clone_backend.model.post.PostDetails(p.postID, p.posterUID, p.title, p.uploadDate) from post_details p where p.postID in :post_ids_list order by p.postID")
    List<PostDetails> getPreviewPostDetailsList(
            @Param("post_ids_list") List<Integer> postIDsList
    );

    @Transactional
    @Modifying
    @Query("update post_details p set p.title = :post_title, p.content = :post_content, p.modifiedDate = :modified_date where p.postID = :post_id")
    void updatePostById(
            @Param("post_id")       int postId,
            @Param("post_title")    String postTitle,
            @Param("post_content")  String postContent,
            @Param("modified_date") LocalDateTime modifiedDate
    );

    @Transactional
    @Modifying
    @Query("update post_details p set p.viewCount = p.viewCount + :view_count where p.postID = :post_id")
    void addViewCount(
            @Param("post_id") int postID,
            @Param("view_count") int viewCount
    );
}
