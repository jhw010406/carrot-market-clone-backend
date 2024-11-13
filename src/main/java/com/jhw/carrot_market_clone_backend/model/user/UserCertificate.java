package com.jhw.carrot_market_clone_backend.model.user;

import jakarta.persistence.*;

@Entity(name = "user_certificate")
public class UserCertificate {

    @Id
    // MySQL의 새로운 값 생성 로직에 따른다. 1부터 시작함.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_uid")
    private Integer uid;

    @Column(name = "user_id")
    private String  id;

    @Column(name = "user_password")
    private String  password;

    public UserCertificate(
            Integer uid,
            String id,
            String password
    ) {
        this.uid = uid;
        this.id = id;
        this.password = password;
    }

    public UserCertificate() {}

    public String getId() {
        return this.id;
    }

    public String getPassword() {
        return this.password;
    }

    public Integer getUid() { return this.uid; }

    public void setUid(Integer uid) { this.uid = uid; }
}
