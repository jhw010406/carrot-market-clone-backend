package com.jhw.carrot_market_clone_backend.repository;

import com.jhw.carrot_market_clone_backend.model.post.PostImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, String> {

    @Transactional
    @Modifying
    @Query("update post_image_hash p set p.postId = :post_id, p.imageNumber = :image_number where p.name = :filename")
    void updatePostIdAndImageNumberById(
            @Param("filename")  String  filename,
            @Param("post_id")   int     postID,
            @Param("image_number")  int     imageNumber
    );

    @Query("select new com.jhw.carrot_market_clone_backend.model.post.PostImage(p.postId, p.url) from post_image_hash p where p.postId in :post_ids and p.imageNumber = 0 order by p.postId")
    List<PostImage> findAllThumbnailsByPostIdsList(
            @Param("post_ids") List<Integer> postIDsList
    );

    // List< Pair<image_url, image_number> >
    @Query("select new org.springframework.data.util.Pair(p.url, p.imageNumber) from post_image_hash p where p.postId = :post_id order by p.imageNumber")
    List< Pair<String, Integer> > findAllImagesByPostId(
            @Param("post_id") int postID
    );
}
