package com.example.Fooddeliverysystem.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.Date;

@ApiModel
@Entity
@NamedQuery(name = "ViberSender.findAll", query = "SELECT v FROM ViberSender v")
public class ViberSender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int viberSenderId;

    private String userId;

    @Temporal(TemporalType.DATE)
    private Date time;

    public int getViberSenderId() {
        return viberSenderId;
    }

    public void setViberSenderId(int viberSenderId) {
        this.viberSenderId = viberSenderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}