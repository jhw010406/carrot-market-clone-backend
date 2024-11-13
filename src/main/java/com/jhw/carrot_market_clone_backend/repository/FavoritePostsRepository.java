package com.jhw.carrot_market_clone_backend.repository;

import com.jhw.carrot_market_clone_backend.model.post.favorite.FavoritePosts;
import com.jhw.carrot_market_clone_backend.model.post.favorite.FavoritePostsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoritePostsRepository extends JpaRepository<FavoritePosts, FavoritePostsKey> {

    @Query("select f.id.postId from favorite_posts f where f.id.userUid = :user_uid")
    List<Integer> findByUserUid(@Param("user_uid") int userUid);
}
