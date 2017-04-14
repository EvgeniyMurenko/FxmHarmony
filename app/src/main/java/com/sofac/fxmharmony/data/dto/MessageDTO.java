package com.SoftwareFactory.dto;

import java.util.Date;
import java.util.Set;


public class MessageDTO {

    public MessageDTO(){}

    public MessageDTO(Date messageTime, String messageText, String isRead, Set<String> filesUrl) {
        this.messageTime = messageTime;
        this.messageText = messageText;
        this.isRead = isRead;
        this.filesUrl = filesUrl;
    }

    private Date messageTime;

    private String messageText;

    private String isRead;

    private Set<String> filesUrl;

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Set<String> getFilesUrl() {
        return filesUrl;
    }

    public void setFilesUrl(Set<String> filesUrl) {
        this.filesUrl = filesUrl;
    }
}
