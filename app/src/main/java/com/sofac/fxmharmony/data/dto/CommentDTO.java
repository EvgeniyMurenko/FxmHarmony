package com.sofac.fxmharmony.data.dto;

import java.util.Date;

public class CommentDTO {

    public CommentDTO(Long id, Long userID, Date date, String commentText, Long postID) {
        this.id = id;
        this.userID = userID;
        this.date = date;
        this.commentText = commentText;
        this.postID = postID;
    }

    private Long id;

    private Long userID;

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
}
