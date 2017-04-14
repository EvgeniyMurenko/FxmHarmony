package com.SoftwareFactory.dto;


import com.SoftwareFactory.dto.MessageDTO;

import java.util.Date;
import java.util.List;


public class CaseDTO {

    public CaseDTO(){}

    public CaseDTO(String projectTitle, String status, Date creationDate, List<MessageDTO> messages) {
        this.projectTitle = projectTitle;
        this.status = status;
        this.creationDate = creationDate;
        this.messages = messages;
    }

    private String projectTitle;


    private String status;


    private Date creationDate;


    private List<MessageDTO> messages;

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }
}
