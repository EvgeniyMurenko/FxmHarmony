package com.sofac.fxmharmony.data.dto;


import java.util.Date;

public class MessageTask {

    public MessageTask(){}

    public MessageTask(String title, String messageText, Date date, Boolean isApprove) {
        this.title = title;
        this.messageText = messageText;
        this.date = date;
        this.isApprove = isApprove;
    }

    public MessageTask(String title, String messageText, Date date) {
        this.title = title;
        this.messageText = messageText;
        this.date = date;
    }

    private String title;

    private String messageText;

    private Date date;

    private Boolean isApprove;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getApprove() {
        return isApprove;
    }

    public void setApprove(Boolean approve) {
        isApprove = approve;
    }
}
