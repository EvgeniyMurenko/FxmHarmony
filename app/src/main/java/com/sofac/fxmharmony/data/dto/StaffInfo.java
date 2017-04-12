package com.sofac.fxmharmony.data.dto;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class StaffInfo implements Serializable {


    public StaffInfo(Long id, String name, String phone, String email, Date birthday, List<MessageTask> messageTasks) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.messageTasks = messageTasks;
    }

    private Long id;

    private String name;

    private String phone;

    private String email;

    private Date birthday;

    @SerializedName("messageTaskDTOS")
    private List<MessageTask> messageTasks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<MessageTask> getMessageTasks() {
        return messageTasks;
    }

    public void setMessageTasks(List<MessageTask> messageTasks) {
        this.messageTasks = messageTasks;
    }

    public Long getId() {        return id;    }

    public void setId(Long id) {        this.id = id;    }

    @Override
    public String toString() {
        return "StaffInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", messageTasks=" + messageTasks +
                '}';
    }
}
