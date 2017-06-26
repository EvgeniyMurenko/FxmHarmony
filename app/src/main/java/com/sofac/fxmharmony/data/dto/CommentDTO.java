package com.sofac.fxmharmony.data.dto;

import java.io.Serializable;
import java.util.Date;

public class CommentDTO  implements Serializable {

    public CommentDTO(Long id, Long userID, Date date, String commentText, Long postID) {
        this.id = id;
        this.userID = userID;
        this.date = date;
        this.commentText = commentText;
        this.postID = postID;
    }

    public CommentDTO(Long id, Long userID, String userName, Date date, String commentText, Long postID) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.date = date;
        this.commentText = commentText;
        this.postID = postID;
    }

    private Long id;

    private Long userID;

    private String userName;

    private Date date;

    private String commentText;

    private Long postID;

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

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
