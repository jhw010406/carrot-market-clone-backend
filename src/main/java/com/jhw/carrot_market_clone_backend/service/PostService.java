package com.jhw.carrot_market_clone_backend.service;

import com.jhw.carrot_market_clone_backend.model.exception.ServerErrorException;
import com.jhw.carrot_market_clone_backend.model.post.*;
import com.jhw.carrot_market_clone_backend.model.post.favorite.FavoritePosts;
import com.jhw.carrot_market_clone_backend.model.post.favorite.FavoritePostsKey;
import com.jhw.carrot_market_clone_backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

enum PostCategories {
    TRADING(0),
    COMMUNITY(1);

    public final int value;
    PostCategories(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}

@Service
public class PostService {

    @Value("${aws.cloudfront.s3.domain}")
    private String imageBaseURL;

    private final AwsS3Service awsS3Service;

    private final UserCertificateRepository userCertificateRepository;
    private final PostersRepository postersRepository;
    private final PostDetailsRepository postDetailsRepository;
    private final PostDetailsTradingRepository postDetailsTradingRepository;
    private final PostDetailsCommunityRepository postDetailsCommunityRepository;
    private final PostImageRepository postImageRepository;
    private final FavoritePostsRepository favoritePostsRepository;


    public PostService(
            AwsS3Service awsS3Service,
            UserCertificateRepository userCertificateRepository,
            PostersRepository postersRepository,
            PostDetailsRepository postDetailsRepository,
            PostDetailsTradingRepository postDetailsTradingRepository,
            PostDetailsCommunityRepository postDetailsCommunityRepository,
            PostImageRepository postImageRepository,
            FavoritePostsRepository favoritePostsRepository
    ) {
        this.awsS3Service = awsS3Service;
        this.userCertificateRepository = userCertificateRepository;
        this.postersRepository = postersRepository;
        this.postDetailsRepository = postDetailsRepository;
        this.postDetailsTradingRepository = postDetailsTradingRepository;
        this.postDetailsCommunityRepository = postDetailsCommunityRepository;
        this.postImageRepository = postImageRepository;
        this.favoritePostsRepository = favoritePostsRepository;
    }

    public PostDetails getPostDetails(
            int inputPostId
    ) throws EntityNotFoundException {
        PostDetails                     postDetails;
        String                          posterID;
        PostDetailsTrading postDetailsTrading;
        PostDetailsCommunity postDetailsCommunity;
        List<PostImage> postImages;

        // add view count
        postDetailsRepository.addViewCount(inputPostId, 1);


        // get post details
        postDetails = postDetailsRepository
                .findById(inputPostId)
                .orElseThrow(() -> new EntityNotFoundException("post details not found"));


        // get poster ID
        posterID = userCertificateRepository
                .findIdByUid(postDetails.getPosterUID())
                .orElseThrow(() -> new EntityNotFoundException("poster ID not found"));
        postDetails.setPosterID(posterID);


        // get post images list, it is made of pair(image_url, image_number)
        postImages = postImageRepository
                .findAllImagesByPostId(inputPostId);


        // get post details {category}
        if (postDetails.getCategory() == PostCategories.TRADING.getValue()){
            postDetailsTrading = postDetailsTradingRepository
                    .findById(inputPostId)
                    .orElseThrow(() -> new EntityNotFoundException("post details trading not found"));

            postDetailsTrading.setProductImagesForGet(postImages);
            postDetails.setPostDetailsTrading(postDetailsTrading);
        }
        else{
            postDetailsCommunity = postDetailsCommunityRepository
                    .findById(inputPostId)
                    .orElseThrow(() -> new EntityNotFoundException("post details community not found"));
        }

        return postDetails;
    }

    public List<PostDetails> getPreviewPostsList(
            int inputCategory,
            Integer inputUserUID,
            List<Integer> inputPostIdsList,
            int inputStartPageNumber,
            int inputPageCount
    ) throws Exception {
        int idx;
        int imgIdx;
        int idsListSize;
        int thumbnailsListSize;
        List<Integer> postIdsList = inputPostIdsList;
        List<PostDetails> postDetailsList;
        List<PostDetailsTrading> postDetailsTradingList = null;
        List<PostDetailsCommunity> postDetailsCommunityList = null;
        List<PostImage> postThumbnailsList;

        // get postIDs list from posters table
        if (postIdsList == null) {
            if (inputUserUID == null) {
                postIdsList = postersRepository.getPostIdsList(
                        inputCategory,
                        inputPageCount,
                        inputStartPageNumber
                );
            }
            else {
                postIdsList = postersRepository.getPostIdsListForUser(
                        inputUserUID,
                        inputCategory,
                        inputPageCount,
                        inputStartPageNumber
                );
            }
        }
        if (postIdsList == null || postIdsList.isEmpty())
            throw new Exception("any posters not exist");


        // get post details list from post_details table
        postDetailsList = postDetailsRepository.getPreviewPostDetailsList(postIdsList);
        if (postDetailsList == null || postDetailsList.isEmpty())
            throw new Exception("any post_details not exist");


        // get post details for trading or community from post_details_trading table or post_details_community table
        if (inputCategory == PostCategories.TRADING.getValue()){
            postDetailsTradingList = postDetailsTradingRepository.getPreviewPostDetailsTradingList(postIdsList);
            if (postDetailsTradingList == null || postDetailsTradingList.isEmpty())
                throw new Exception("any post_details_trading not exist");
        }
        else {}


        // get post's thumbnail images from post_image_hash
        postThumbnailsList = postImageRepository.findAllThumbnailsByPostIdsList(postIdsList);


        // combine data for post details to preview
        idsListSize = postIdsList.size();
        thumbnailsListSize = postThumbnailsList.size();
        for (idx = 0; idx < idsListSize; idx++){

            // assign object of postDetails{category}List to postDetails object
            if (inputCategory == PostCategories.TRADING.getValue()){
                postDetailsList.get(idx).setPostDetailsTrading(postDetailsTradingList.get(idx));
            }
            else{}


            // assign thumbnail url to postDetails object
            for (imgIdx = 0; imgIdx < thumbnailsListSize; imgIdx++){
                if (postDetailsList.get(idx).getPostID() == postThumbnailsList.get(imgIdx).getPostId()){
                    postDetailsList.get(idx).setThumbnailImageUrl(postThumbnailsList.get(imgIdx).getUrl());
                    break;
                }
            }
        }

        return postDetailsList;
    }

    public List<PostDetails> getPreviewFavoritePostsList(
            Integer inputUserUid,
            int inputStartPageNumber,
            int inputPageCount
    ) throws Exception {
        List<Integer> postIdsList;

        postIdsList = favoritePostsRepository.findByUserUid(inputUserUid);

        if (postIdsList != null && !postIdsList.isEmpty()){

            return getPreviewPostsList(
                    PostCategories.TRADING.getValue(),
                    inputUserUid,
                    postIdsList,
                    inputStartPageNumber,
                    inputPageCount
            );
        }
        else {
            throw new ServerErrorException("any posts not found");
        }
    }

    public void uploadPost(
            PostDetails inputPostDetails
    ) throws EntityNotFoundException {
        // List< Pair<filename, image_index> >
        List<Pair<String, Integer>> postImages = null;
        int postImagesCount = 0;
        String postImageFilename;
        Posters posters;

        // insert data to poster table
        posters = new Posters(null, inputPostDetails.getPosterUID(), inputPostDetails.getCategory());
        postersRepository.save(posters);


        // insert data to post_details table
        inputPostDetails.setPostID(posters.getPostID());
        inputPostDetails.setUploadDate(LocalDateTime.now());
        inputPostDetails.setModifiedDate(LocalDateTime.now());
        postDetailsRepository.save(inputPostDetails);


        // insert data to post_details_trading table or post_details_community table
        if (posters.getCategory() == PostCategories.TRADING.getValue()) {
            postImages = inputPostDetails.postDetailsTrading.getProductImagesForUpload();
            postImagesCount = postImages.size();

            inputPostDetails.postDetailsTrading.setPostID(posters.getPostID());
            postDetailsTradingRepository.save(inputPostDetails.postDetailsTrading);
        } else {
            inputPostDetails.postDetailsCommunity.setId(posters.getPostID());
            postDetailsCommunityRepository.save(inputPostDetails.postDetailsCommunity);
        }


        // set data in post_image_hash table
        for (int idx = 0; idx < postImagesCount; idx++) {
            postImageFilename = postImages.get(idx).getFirst();

            if (postImageRepository.existsById(postImageFilename)) {
                postImageRepository.updatePostIdAndImageNumberById(
                        postImageFilename,
                        posters.getPostID(),
                        idx
                );
            } else {
                throw new EntityNotFoundException(postImageFilename + " not found");
            }
        }
    }

    public void updatePost(
            int postId,
            PostDetails inputPostDetails
    ) throws EntityNotFoundException {

        if (postersRepository.findById(postId).isPresent()) {
            postDetailsRepository.updatePostById(
                    postId,
                    inputPostDetails.getTitle(),
                    inputPostDetails.getContent(),
                    LocalDateTime.now()
            );

            postDetailsTradingRepository.updateProductPriceById(
                    postId,
                    inputPostDetails.postDetailsTrading.getProductPrice()
            );

            postImageRepository.deleteAllByPostId(inputPostDetails.getPostID());
            postImageRepository.saveAll(inputPostDetails.postDetailsTrading.getProductImagesForUpdate());
        }
        else {
            throw new EntityNotFoundException("post details not found");
        }
    }

    public void deletePost(
            int inputPostId
    ) throws EntityNotFoundException {
        Optional<Posters>   posterId;
        String              userId = SecurityContextHolder.getContext().getAuthentication().getName();
        int                 userUid = userCertificateRepository.findUidByUserId(userId);

        posterId = postersRepository.findById(inputPostId);

        if (posterId.isPresent()){

            if (posterId.get().getUserUID() == userUid){
                postersRepository.deleteById(inputPostId);
            }
            else {
                throw new EntityNotFoundException("poster UID is different");
            }
        }
        else {
            throw new EntityNotFoundException("poster ID not found");
        }
    }

    public PostImage getPreSignedURL(
            Integer postId,
            String inputFilename,
            String inputContentType
    ) throws ServiceException {
        String      filename = null;
        String      nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("_yyyyMMddHHmmssSS"));
        String      preSignedUrl = null;
        PostImage   response = null;

        if (postId < 1){ postId = null; }

        for (int idx = inputFilename.length() - 1; idx >= 0; idx--){
            if (inputFilename.charAt(idx) == '.'){
                filename = inputFilename.substring(0, idx) + nowTime + inputFilename.substring(idx);
                break;
            }
        }

        // get pre-signed url about object
        try {
            preSignedUrl = awsS3Service.createPreSignedUrlForUpload(filename, inputContentType);
        } catch (Exception e) {
            throw new ServiceException("Get pre-signed URL failed");
        }

        // create image object
        try {
            response = new PostImage(
                    filename,
                    null,
                    inputContentType,
                    preSignedUrl,
                    imageBaseURL + filename,
                    postId
            );
            postImageRepository.save(response);
        } catch (Exception e) {
            throw new ServiceException("Create Object failed");
        }

        return response;
    }

    public void deletePostImage(
            String inputFilename
    ) throws RuntimeException {

        if (postImageRepository.existsById(inputFilename)) {

            try {
                awsS3Service.deleteObject(inputFilename);
            } catch (ServiceException e) {
                throw new ServerErrorException("Delete " + inputFilename + " failed from Storage");
            }

            try {
                postImageRepository.deleteById(inputFilename);
            } catch (ServiceException e) {
                throw new ServerErrorException("Delete " + inputFilename + " failed from DB");
            }
        }
        else {
            throw new EntityNotFoundException("Post image not found");
        }
    }

    public Boolean isFavoritedPost(
            int userUid,
            int postId
    ) throws EntityNotFoundException {
        FavoritePostsKey key = new FavoritePostsKey(userUid, postId);
        boolean isFavoritedPost = false;

        try {
            isFavoritedPost = favoritePostsRepository.existsById(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return isFavoritedPost;
    }

    public void favoritePost(
            int userUid,
            int postId
    ) throws ServerErrorException {
        FavoritePostsKey key = new FavoritePostsKey(userUid, postId);

        if (favoritePostsRepository.findById(key).isEmpty()){
            favoritePostsRepository.save(new FavoritePosts(key));
            postDetailsTradingRepository.favoritePost(postId);
        }
        else {
            throw new ServerErrorException("favorite post already exists");
        }
    }

    public void cancelFavoritePost(
            int userUid,
            int postId
    ) throws ServerErrorException {
        FavoritePostsKey key = new FavoritePostsKey(userUid, postId);

        if (favoritePostsRepository.findById(key).isPresent()) {
            favoritePostsRepository.deleteById(key);
            postDetailsTradingRepository.unfavoritePost(postId);
        }
        else {
            throw new ServerErrorException("favorite post not found");
        }
    }
}
