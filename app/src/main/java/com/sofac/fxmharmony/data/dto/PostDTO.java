package com.sofac.fxmharmony.data.dto;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class PostDTO extends SugarRecord implements Serializable {


    public PostDTO() {}

    public PostDTO(Long id, Long userID, String userName, Date date, String postText) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.date = date;
        this.postText = postText;
    }

    private Long id;
    private Long userID;
    private String userName;
    private Date date;
    private String postText;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
