package com.jhw.carrot_market_clone_backend.model.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.springframework.data.util.Pair;

import java.util.List;

@Entity(name = "post_details_trading")
public class PostDetailsTrading {

    @Id
    @Column(name = "post_id")
    private int postID;

    // List< Pair<filename, image_number> >
    @Transient
    private List< Pair<String, Integer> > productImagesForUpload;

    // List< Pair<image_url, image_number> >
    @Transient
    private List< Pair<String, Integer> > productImagesForGet;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "model_year")
    private int modelYear;

    @Column(name = "mileage")
    private int mileage;

    @Column(name = "chat_count")
    private int chatCount;

    @Column(name = "favorite_count")
    private int favoriteCount;

    public PostDetailsTrading(
            int postID,
            List< Pair<String, Integer> > productImagesForUpload,
            List< Pair<String, Integer> > productImagesForGet,
            String productType,
            int productPrice,
            int modelYear,
            int mileage,
            int chatCount,
            int favoriteCount
    ){
        this.postID = postID;
        this.productImagesForUpload = productImagesForUpload;
        this.productImagesForGet = productImagesForGet;
        this.productType = productType;
        this.productPrice = productPrice;
        this.modelYear = modelYear;
        this.mileage = mileage;
        this.chatCount = chatCount;
        this.favoriteCount = favoriteCount;
    }

    public PostDetailsTrading(
            int productPrice,
            int favoriteCount
    ) {
        this.productPrice = productPrice;
        this.favoriteCount = favoriteCount;
    }

    public PostDetailsTrading(){}

    public void setPostID(int postID) { this.postID = postID; }

    public int getPostID() { return postID; }

    public String getProductType() { return productType; }

    public int getProductPrice() { return productPrice; }

    public void setProductPrice(int productPrice) { this.productPrice = productPrice; }

    public int getModelYear() { return modelYear; }

    public int getMileage() { return mileage; }

    public int getChatCount() { return chatCount; }

    public int getFavoriteCount() { return favoriteCount; }

    public List<Pair<String, Integer>> getProductImagesForUpload() { return productImagesForUpload; }

    public void setProductImagesForUpload(List<Pair<String, Integer>> productImagesForUpload) { this.productImagesForUpload = productImagesForUpload; }

    public List<Pair<String, Integer>> getProductImagesForGet() { return productImagesForGet; }

    public void setProductImagesForGet(List<Pair<String, Integer>> productImagesForGet) { this.productImagesForGet = productImagesForGet; }
}
