package com.jhw.carrot_market_clone_backend.repository;

import com.jhw.carrot_market_clone_backend.model.post.PostDetailsTrading;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostDetailsTradingRepository extends JpaRepository<PostDetailsTrading, Integer> {

    @Query("select new com.jhw.carrot_market_clone_backend.model.post.PostDetailsTrading(p.productPrice, p.favoriteCount) from post_details_trading p where p.postID in :post_ids_list order by p.postID")
    List<PostDetailsTrading> getPreviewPostDetailsTradingList(@Param("post_ids_list") List<Integer> postIDsList);

    @Transactional
    @Modifying
    @Query("update post_details_trading p set p.favoriteCount = p.favoriteCount + 1 where p.postID = :post_id")
    void favoritePost(@Param("post_id") int post_id);

    @Transactional
    @Modifying
    @Query("update post_details_trading p set p.favoriteCount = p.favoriteCount - 1 where p.postID = :post_id")
    void unfavoritePost(@Param("post_id") int post_id);
}
