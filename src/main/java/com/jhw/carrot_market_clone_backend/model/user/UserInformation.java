package com.jhw.carrot_market_clone_backend.model.user;

import jakarta.persistence.*;

@Entity(name = "user_information")
public class UserInformation {

    @Id
    @Column(name = "user_uid")
    private Integer uid;

    // transient : DB에 매핑시키지 않는다.
    @Column(name = "user_id")
    private String id;

    @Transient
    private String password;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "user_age")
    private Integer age;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Transient
    private int numberPosts;

    public UserInformation(
            Integer uid,
            String id,
            String password,
            String nickname,
            Integer age,
            String phoneNumber,
            int numberPosts
    ) {
        this.uid = uid;
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.numberPosts = numberPosts;
    }

    public UserInformation(
            Integer uid,
            String id,
            Integer age
    ) {
        this.uid = uid;
        this.id = id;
        this.nickname = id;
        this.age = age;
    }

    public UserInformation() {}

    public int getUid() {
        return this.uid;
    }

    public String getNickname() { return this.nickname; }

    public Integer getAge() { return this.age; }

    public String getPhoneNumber() { return this.phoneNumber; }
}
