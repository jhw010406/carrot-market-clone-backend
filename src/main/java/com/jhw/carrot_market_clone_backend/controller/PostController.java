package com.jhw.carrot_market_clone_backend.controller;

import com.jhw.carrot_market_clone_backend.model.post.*;
import com.jhw.carrot_market_clone_backend.model.exception.ServerErrorException;
import com.jhw.carrot_market_clone_backend.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PostController {

    private final PostService postService;

    public PostController(
            PostService postService
    ) {
        this.postService = postService;
    }

    @GetMapping("/post")
    public ResponseEntity<PostDetails> getPostDetils(
            @RequestParam(value = "post-id") int inputPostId
    ){
        PostDetails postDetails = null;

        try {
            postDetails = postService.getPostDetails(inputPostId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Post not found");
        }

        return ResponseEntity.ok(postDetails);
    }


    // upload post
    @PostMapping("/post")
    public void uploadPost(@RequestBody PostDetails inputPostDetails)
    {
        try {
            postService.uploadPost(inputPostDetails);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e);
        }
    }


    @PostMapping("/update-post")
    public void updatePost(
            @RequestParam("user-uid") int inputUserUid,
            @RequestBody PostDetails inputPostDetails
    ) {
        int userUid = inputPostDetails.getPosterUID();

        if (userUid != inputUserUid){
            throw new ServerErrorException("user id is different with poster's id");
        }

        postService.updatePost(
                inputPostDetails.getPostID(),
                inputPostDetails
        );
    }


    @DeleteMapping("/post")
    public void deletePost(
            @RequestParam("post-id") int inputPostId
    ){
        try {
            postService.deletePost(inputPostId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e);
        }
    }


    // get posts list
    @GetMapping("/posts-list/{category}")
    public ResponseEntity< List<PostDetails> > getPreviewPostsList(
            @PathVariable("category") int inputCategory,
            @RequestParam(value = "user-uid", required = false) Integer inputUserUID,
            @RequestParam("page-number") int inputStartPageNumber,
            @RequestParam("page-count") int inputPageCount
    ) {
        List<PostDetails> postDetailsList = null;

        try {
            postDetailsList = postService.getPreviewPostsList(
                    inputCategory,
                    inputUserUID,
                    null,
                    inputStartPageNumber,
                    inputPageCount
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(postDetailsList);
    }


    @GetMapping("/favorite-posts-list")
    public ResponseEntity< List<PostDetails> > getPreviewFavoritePostsList(
            @RequestParam("user-uid") Integer inputUserUid,
            @RequestParam("page-number") int inputStartPageNumber,
            @RequestParam("page-count") int inputPageCount
    ) {
        List<PostDetails> postDetailsList = null;

        if (inputUserUid != null){

            try {
                postDetailsList = postService.getPreviewFavoritePostsList(inputUserUid, inputStartPageNumber, inputPageCount);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new ServerErrorException("request user uid is null");
        }

        return ResponseEntity.ok(postDetailsList);
    }


    @GetMapping("/favorite-post")
    public ResponseEntity<Boolean> isFavoritedPost(
            @RequestParam("user-uid") int userUid,
            @RequestParam("post-id") int postId
    ) {
        boolean isFavorited = false;

        try {
            isFavorited = postService.isFavoritedPost(userUid, postId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(isFavorited);
    }


    @PostMapping("/favorite-post")
    public void favoritePost(
            @RequestParam("user-uid") int userUid,
            @RequestParam("post-id") int postId
    ) {
        try {
            postService.favoritePost(userUid, postId);
        } catch (ServerErrorException e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/favorite-post")
    public void cancelFavoritePost(
            @RequestParam("user-uid") int userUid,
            @RequestParam("post-id") int postId
    ) {
        try {
            postService.cancelFavoritePost(userUid, postId);
        } catch (ServerErrorException e) {
            throw new RuntimeException(e);
        }
    }


    // get pre-signed url about image
    @GetMapping("/post-image")
    public ResponseEntity<PostImage> getPreSignedURL(
            @RequestParam("post-id") Integer postId,
            @RequestParam("filename") String inputFilename,
            @RequestParam("contentType") String inputContentType
    ) {
        PostImage postImage = null;

        try {
            postImage = postService.getPreSignedURL(postId, inputFilename, inputContentType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(postImage);
    }


    @DeleteMapping("/post-image")
    public void deletePostImage(
            @RequestParam("filename") String inputFilename
    ) {

        try {
            postService.deletePostImage(inputFilename);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
