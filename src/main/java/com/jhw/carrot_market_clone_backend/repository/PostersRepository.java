package com.jhw.carrot_market_clone_backend.repository;

import com.jhw.carrot_market_clone_backend.model.post.Posters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostersRepository extends JpaRepository<Posters, Integer> {

    @Query("select p.postID from posters p where p.category = :post_category order by p.postID limit :posts_count offset :start_number")
    List<Integer> getPostIDsList(
            @Param("post_category") int inputPostCategory,
            @Param("posts_count") int inputPostsCount,
            @Param("start_number") int inputStartNumber
    );

    @Query("select p.postID from posters p where p.category = :post_category and p.posterUID = :user_uid order by p.postID limit :posts_count offset :start_number")
    List<Integer> getPostIDsListForUser(
            @Param("user_uid")  int inputUserUid,
            @Param("post_category") int inputPostCategory,
            @Param("posts_count") int inputPostsCount,
            @Param("start_number") int inputStartNumber
    );
}
