package com.sofac.fxmharmony.data.dto;


import java.sql.Date;
import java.util.Set;

public class StaffInfo {

    public StaffInfo(){}

    public StaffInfo(String name, String phone, String email, Date birthday, Set<MessageTask> messageTasks) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.messageTasks = messageTasks;
    }

    private String name;

    private String phone;

    private String email;

    private Date birthday;

    private Set<MessageTask> messageTasks;

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

    public Set<MessageTask> getMessageTasks() {
        return messageTasks;
    }

    public void setMessageTasks(Set<MessageTask> messageTasks) {
        this.messageTasks = messageTasks;
    }
}
